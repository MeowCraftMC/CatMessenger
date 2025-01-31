package cx.rain.mc.catmessenger.connector;

import com.rabbitmq.client.*;
import cx.rain.mc.catmessenger.CatMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MessageQueue {
    public static final String EXCHANGE_NAME = "messages";
    public static final String EXCHANGE_TYPE = "fanout";

    private final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    private final String clientId;
    private final String queueName;
    private final Connection connection;
    private final int maxRetryCount;

    private Channel channel;
    private boolean isClosing = false;

    private final List<IMessageHandler> handlers = new ArrayList<>();

    public MessageQueue(String clientId, Connection connection, int maxRetryCount) {
        this.clientId = clientId;
        this.queueName = "message." + clientId;
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
            channel.queueDeclare(clientId, false, true, true, null).getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");

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

                        if (message.getClient().equals(clientId)) {
                            return;
                        }

                        for (var handler : handlers) {
                            try {
                                handler.handle(message);
                            } catch (Exception ex) {
                                logger.error("Error handling message", ex);
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

    public void publish(ConnectorMessage message) {
        message.setClient(clientId);

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

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            internalPublish(message, retry + 1);
        }
    }

    public void addHandler(IMessageHandler handler) {
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
