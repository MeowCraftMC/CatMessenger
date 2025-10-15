package cx.rain.mc.catmessenger.api.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import cx.rain.mc.catmessenger.api.CatMessenger;
import cx.rain.mc.catmessenger.api.utilities.RetryingUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractQueue {

    protected static final int MAX_RETRY = 5;

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractQueue.class);

    protected final CatMessenger messenger;

    @Getter
    private Channel channel;

    // Todo: use future, close after send failed.
    @Getter
    private AtomicInteger sending = new AtomicInteger(0);

    public AbstractQueue(CatMessenger messenger) {
        this.messenger = messenger;
    }

    protected abstract Consumer createConsumer();

    protected abstract String getExchangeName();

    protected abstract String getExchangeType();

    protected abstract String getQueueName();

    protected abstract String getRoutingKey();

    @SneakyThrows
    public void connect() {
        if (channel == null) {
            var connection = messenger.getConnection();
            if (connection == null) {
                throw new IllegalStateException("Connection is null");
            }
            channel = connection.createChannel();
        }

        channel.exchangeDeclare(getExchangeName(), getExchangeType(), true, false, null);
        channel.queueDeclare(getQueueName(), true, true, true, null);
        channel.queueBind(getQueueName(), getExchangeName(), getRoutingKey());

        channel.basicConsume(getQueueName(), false, createConsumer());
    }

    protected void addSending() {
        sending.getAndIncrement();
    }

    protected void removeSending() {
        sending.getAndDecrement();
    }

    @SneakyThrows
    public void disconnect() {
        if (channel.isOpen()) {
            channel.close();
        }
    }

    protected void publish(byte[] bytes) {
        RetryingUtil.runWithRetry(() -> {
                    addSending();
                    publishInternal(bytes);
                },
                MAX_RETRY,
                this::removeSending,
                (ex, tries) -> LOGGER.warn("Publish failed, retrying({}/{}): {}", tries, MAX_RETRY, ex),
                () -> LOGGER.error("All publish retries failed!"));
    }

    protected void ack(long deliveryTag) {
        RetryingUtil.runWithRetry(() -> ackInternal(deliveryTag),
                MAX_RETRY,
                () -> {},
                (ex, tries) -> LOGGER.warn("Ack failed, retrying({}/{}}): {}", tries, MAX_RETRY, ex),
                () -> LOGGER.error("All ack retries failed!"));
    }

    private void publishInternal(byte[] bytes) throws IOException {
        if (!messenger.isConnected() || messenger.isClosing()) {
            return;
        }

        if (getChannel() == null) {
            connect();
        }

        if (!getChannel().isOpen()) {
            getChannel().basicRecover();
        }

        var props = new AMQP.BasicProperties.Builder().appId(messenger.getClientId()).build();
        getChannel().basicPublish(getExchangeName(), getRoutingKey(), props, bytes);
    }

    private void ackInternal(long deliveryTag) throws IOException {
        if (!messenger.isConnected() || messenger.isClosing()) {
            return;
        }

        if (getChannel() == null) {
            connect();
        }

        if (!getChannel().isOpen()) {
            getChannel().basicRecover();
        }

        getChannel().basicAck(deliveryTag, false);
    }
}
