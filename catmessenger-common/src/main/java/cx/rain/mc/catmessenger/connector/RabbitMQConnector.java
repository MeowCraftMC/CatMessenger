package cx.rain.mc.catmessenger.connector;

import com.rabbitmq.client.*;
import cx.rain.mc.catmessenger.CatMessenger;
import cx.rain.mc.catmessenger.message.IMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitMQConnector {
    public static final String EXCHANGE_NAME = "catmessenger";
    public static final String EXCHANGE_TYPE = "fanout";
    public static final String QUEUE_NAME = "messages";
    public static final String ROUTING_KEY = "";

    private final ConnectionFactory factory;

    private final String name;
    private final int maxRetryCount;

    private Connection connection;
    private Channel channel;

    private boolean isClosing = false;

    private final List<IMessageHandler> handlers = new ArrayList<>();

    public RabbitMQConnector(String name, int maxRetryCount, String host, int port, String virtualHost, String username, String password) {
        this.name = name;
        this.maxRetryCount = maxRetryCount;

        factory = new ConnectionFactory();

        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);
    }

    private Connection createConnection() {
        try {
            return factory.newConnection(name);
        } catch (IOException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Channel createChannel() {
        try {
            return connection.createChannel();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void internalConnect() {
        connection = createConnection();
        channel = createChannel();

        try {
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, true, null);
            channel.queueDeclare(QUEUE_NAME, true, false, true, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            {
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        if (isClosing) {
                            return;
                        }

                        var str = new String(body, StandardCharsets.UTF_8);
                        var message = CatMessenger.GSON.fromJson(str, ConnectorMessage.class);

                        for (var handler : handlers) {
                            try {
                                handler.handle(message.getMessage(), message.getSender());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                };

                channel.basicConsume(QUEUE_NAME, true, consumer);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void connect() {
        internalConnect();
    }

    public void publish(IMessage message) {
        var connectorMessage = new ConnectorMessage();
        connectorMessage.setMessage(message);
        connectorMessage.setSender(name);

        var json = CatMessenger.GSON.toJson(connectorMessage);
        internalPublish(json, 0);
    }

    private void internalPublish(String message, int retry) {
        try {
            if (isClosing) {
                return;
            }

            if (retry > maxRetryCount) {
                return;
            }

            if (channel == null || !channel.isOpen()) {
                internalDisconnect();
                internalConnect();
            }

            channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            internalPublish(message, retry + 1);
        }
    }

    public void addHandler(IMessageHandler handler) {
        handlers.add(handler);
    }

    private void internalDisconnect() {
        try {
            if (channel.isOpen()) {
                channel.close();
            }

            if (connection.isOpen()) {
                connection.close();
            }
        } catch (IOException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void disconnect() {
        isClosing = true;
        handlers.clear();
        internalDisconnect();
    }
}
