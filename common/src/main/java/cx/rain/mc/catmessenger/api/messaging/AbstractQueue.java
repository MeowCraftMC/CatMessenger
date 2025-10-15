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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AbstractQueue {

    protected static final int MAX_RETRY = 5;

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractQueue.class);

    protected final CatMessenger messenger;

    @Getter
    private Channel channel;

    private final AMQP.BasicProperties properties;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AbstractQueue(CatMessenger messenger) {
        this.messenger = messenger;
        this.properties = new AMQP.BasicProperties.Builder().appId(messenger.getClientId()).build();
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

    @SneakyThrows
    public void disconnect() {
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    LOGGER.error("Pool did not terminate");
                }
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }

        if (channel.isOpen()) {
            channel.close();
        }
    }

    protected void publish(byte[] bytes) {
        RetryingUtil.runWithRetry(executor,
                () -> publishInternal(bytes),
                MAX_RETRY,
                () -> {},
                (ex, tries) -> LOGGER.warn("Publish failed, retrying({}/{}): {}", tries, MAX_RETRY, ex),
                () -> LOGGER.error("All publish retries failed!"));
    }

    protected void ack(long deliveryTag) {
        RetryingUtil.runWithRetry(executor,
                () -> ackInternal(deliveryTag),
                MAX_RETRY,
                () -> {},
                (ex, tries) -> LOGGER.warn("Ack failed, retrying({}/{}}): {}", tries, MAX_RETRY, ex),
                () -> LOGGER.error("All ack retries failed!"));
    }

    private boolean ensureConnected() throws IOException {
        if (!messenger.isConnected() || messenger.isClosing()) {
            return false;
        }

        if (getChannel() == null) {
            connect();
        }

        if (!getChannel().isOpen()) {
            getChannel().basicRecover();
        }

        return true;
    }

    private void publishInternal(byte[] bytes) throws IOException {
        if (!ensureConnected()) {
            return;
        }

        getChannel().basicPublish(getExchangeName(), getRoutingKey(), properties, bytes);
    }

    private void ackInternal(long deliveryTag) throws IOException {
        if (!ensureConnected()) {
            return;
        }

        getChannel().basicAck(deliveryTag, false);
    }
}
