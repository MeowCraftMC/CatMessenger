package cx.rain.mc.catmessenger.connector;

import com.rabbitmq.client.*;
import cx.rain.mc.catmessenger.CatMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class CommandQueue {
    public static final String EXCHANGE_NAME = "commands";
    public static final String EXCHANGE_TYPE = "topic";

    private final Logger logger = LoggerFactory.getLogger(CommandQueue.class);

    private final String clientId;
    private final String queueName;
    private final Connection connection;
    private final int maxRetryCount;

    private Channel channel;
    private boolean isClosing = false;

    private final List<ICommandHandler> handlers = new ArrayList<>();

    public CommandQueue(String clientId, Connection connection, int maxRetryCount) {
        this.clientId = clientId;
        this.queueName = "command." + clientId;
        this.connection = connection;
        this.maxRetryCount = maxRetryCount;
    }

    public void connect() {
        try {
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, null);
            channel.queueDeclare(queueName, true, false, true, null);
            channel.queueBind(queueName, EXCHANGE_NAME, clientId);

            {
                var consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        if (isClosing) {
                            return;
                        }

                        var str = new String(body, StandardCharsets.UTF_8);
                        var message = CatMessenger.GSON.fromJson(str, ConnectorCommand.class);

                        for (var handler : handlers) {
                            try {
                                handler.handle(message, properties);
                            } catch (Exception ex) {
                                logger.error("Error handling command", ex);
                            }
                        }

                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                };

                channel.basicConsume(queueName, false, consumer);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void publish(String client, ConnectorCommand command) {
        command.setCallback(clientId);
        var json = CatMessenger.GSON.toJson(command);
        internalPublish(client, json, 0);
    }

    public void reply(String callback, String[] message) {
        var json = CatMessenger.GSON.toJson(message);
        internalPublish(callback, json, 0);
    }

    private void internalPublish(String routingKey, String message, int retry) {
        try {
            if (isClosing) {
                return;
            }

            if (retry > maxRetryCount) {
                return;
            }

            if (channel == null || !channel.isOpen()) {
                disconnect();
                connect();
            }

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            internalPublish(routingKey, message, retry + 1);
        }
    }

    public void addHandler(ICommandHandler handler) {
        handlers.add(handler);
    }

    public void disconnect() {
        try {
            isClosing = false;

            if (channel.isOpen()) {
                channel.close();
            }
        } catch (IOException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }
    }
}
