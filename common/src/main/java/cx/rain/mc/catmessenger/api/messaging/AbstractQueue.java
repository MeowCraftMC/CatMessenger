package cx.rain.mc.catmessenger.api.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Supplier;

public abstract class AbstractQueue {

    protected static final int MAX_RETRY = 5;

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractQueue.class);

    @Getter
    private final String clientId;
    private final Supplier<Connection> connection;

    @Getter
    private Channel channel;

    @Getter
    private boolean closed = true;

    public AbstractQueue(String clientId, Supplier<Connection> connection) {
        this.clientId = clientId;
        this.connection = connection;
    }

    protected abstract Consumer createConsumer();

    protected abstract String getExchangeName();

    protected abstract String getExchangeType();

    protected abstract String getQueueName();

    protected abstract String getRoutingKey();

    @SneakyThrows
    public void connect() {
        if (channel == null) {
            channel = connection.get().createChannel();
        }

        closed = false;

        channel.exchangeDeclare(getExchangeName(), getExchangeType(), true, false, null);
        channel.queueDeclare(getQueueName(), true, true, true, null);
        channel.queueBind(getQueueName(), getExchangeName(), getRoutingKey());

        channel.basicConsume(getQueueName(), false, createConsumer());
    }

    @SneakyThrows
    public void disconnect() {
        if (!closed) {
            closed = true;

            if (channel.isOpen()) {
                channel.close();
            }
        }
    }

    protected void publish(byte[] bytes) {
        var retried = 0;
        while (retried <= MAX_RETRY) {
            try {
                if (isClosed()) {
                    return;
                }

                if (getChannel() == null) {
                    connect();
                }

                if (!getChannel().isOpen()) {
                    getChannel().basicRecover();
                }

                var props = new AMQP.BasicProperties.Builder().appId(clientId).build();
                getChannel().basicPublish(getExchangeName(), getRoutingKey(), props, bytes);
                return;
            } catch (IOException ex) {
                retried += 1;
                LOGGER.warn("Publish failed, retrying({}/{}) {}", retried, MAX_RETRY, ex);
            }
        }
        LOGGER.error("All publish retries failed!");
    }

    protected void ack(long deliveryTag) {
        var retried = 0;
        while (retried <= MAX_RETRY) {
            try {
                if (isClosed()) {
                    return;
                }

                if (getChannel() == null) {
                    connect();
                }

                if (!getChannel().isOpen()) {
                    getChannel().basicRecover();
                }

                getChannel().basicAck(deliveryTag, false);
                return;
            } catch (IOException ex) {
                retried += 1;
                LOGGER.warn("Ack failed, retrying({}/{}}) {}", retried, MAX_RETRY, ex);
            }
        }
        LOGGER.error("All ack retries failed!");
    }
}
