package cx.rain.mc.catmessenger.connector;

import com.rabbitmq.client.*;
import cx.rain.mc.catmessenger.CatMessenger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MessageQueue {

    public static final String EXCHANGE_NAME = "catmessenger";
    public static final String EXCHANGE_TYPE = "fanout";
    public static final String ROUTING_KEY = "";

    private final String name;
    private final Connection connection;
    private final int maxRetryCount;

    private Channel channel;
    private String queueName;
    private boolean isClosing = false;

    private final List<IMessageHandler> handlers = new ArrayList<>();

    public MessageQueue(String clientName, Connection connection, int maxRetryCount) {
        this.name = clientName;
        this.connection = connection;
        this.maxRetryCount = maxRetryCount;
    }

    public void connect() {
        try {
            channel = connection.createChannel();

            var messageArgs = new HashMap<String, Object>();
            messageArgs.put("x-message-ttl", 60 * 1000);

            channel.basicQos(1);

            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, messageArgs);
            queueName = channel.queueDeclare(name, false, true, true, null).getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);

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

                        if (message.getClient().equals(name)) {
                            return;
                        }

                        for (var handler : handlers) {
                            try {
                                handler.handle(message);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                };

                channel.basicConsume(queueName, true, consumer);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void publish(ConnectorMessage message) {
        message.setClient(name);

        if (message.getTime() == null) {
            message.setTime(ZonedDateTime.now());
        }

        var json = CatMessenger.GSON.toJson(message);
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
                disconnect();
                connect();
            }

            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            internalPublish(message, retry + 1);
        }
    }

    public void addHandler(IMessageHandler handler) {
        handlers.add(handler);
    }

    public void disconnect() {
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
}
