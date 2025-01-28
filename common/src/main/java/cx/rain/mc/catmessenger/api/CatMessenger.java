package cx.rain.mc.catmessenger.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cx.rain.mc.catmessenger.api.message.Message;
import cx.rain.mc.catmessenger.api.utilities.RabbitMQHelper;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.rabbitmq.QueueOptions;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CatMessenger {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(CatMessenger.class);

    private final String clientId;

    private final Vertx vertx;
    private final RabbitMQClient client;

    private final List<Consumer<Message>> messageConsumerList = new ArrayList<>();

    private final int retried = 0;

    public CatMessenger(String clientId,
                        String host, int port, String username, String password, String virtualHost,
                        int maxRetry, long retryInterval) {
        this.clientId = clientId;

        this.vertx = Vertx.vertx();

        var config = new RabbitMQOptions();
        config.setHost(host);
        config.setPort(port);
        config.setUser(username);
        config.setPassword(password);
        config.setVirtualHost(virtualHost);
        config.setAutomaticRecoveryEnabled(true);
        config.setAutomaticRecoveryOnInitialConnection(true);
        config.setReconnectAttempts(maxRetry);
        config.setReconnectInterval(retryInterval);
        this.client = RabbitMQClient.create(vertx, config);
    }

    public void connect() {
        client.start(result -> {
            if (result.failed()) {
                LOGGER.error("Couldn't connect to the RabbitMQ host!", result.cause());
            }
        });

        var messageExchange = RabbitMQHelper.getMessageExchange();
        var messageQueue = RabbitMQHelper.getMessageQueue(clientId);

        client.exchangeDeclare(messageExchange, "fanout", true, false, result -> {
            if (result.failed()) {
                LOGGER.error("Couldn't declare exchange {}", messageExchange, result.cause());
            }
        });

        client.queueDeclare(messageQueue, true, true, true, result -> {
            if (result.failed()) {
                LOGGER.error("Couldn't declare queue {}", messageQueue, result.cause());
            }
        });

        client.queueBind(messageQueue, messageExchange, "", result -> {
            if (result.failed()) {
                LOGGER.error("Couldn't bind queue {} to {}", messageQueue, messageExchange, result.cause());
            }
        });

        client.basicConsumer(messageQueue, new QueueOptions().setAutoAck(false), result -> {
            if (result.failed()) {
                LOGGER.warn("Consume failed {}", messageQueue, result.cause());
                return;
            }

            result.result().handler(rabbitMQMessage -> {
                var data = rabbitMQMessage.body().toString();
                try {
                    var message = GSON.fromJson(data, Message.class);
                    messageConsumerList.forEach(c -> c.accept(message));
                    client.basicAck(rabbitMQMessage.envelope().getDeliveryTag(), false);
                } catch (Throwable ex) {
                    LOGGER.warn("Error during handling data {}", data, ex);
                }
            });
        });
    }

    public void disconnect() {
        client.stop(result -> {
            if (result.failed()) {
                LOGGER.error("Something wrong while disconnecting", result.cause());
            }
        });
    }

    public void consumeMessage(Consumer<Message> messageConsumer) {
        messageConsumerList.add(messageConsumer);
    }

    public void sendMessage(Message message) {
        var exchange = RabbitMQHelper.getMessageExchange();
        var body = GSON.toJson(message);
        client.basicPublish(exchange, "", Buffer.buffer(body), result -> {
            if (result.failed()) {
                LOGGER.error("Send failed {}", exchange, result.cause());
            }
        });
    }
}
