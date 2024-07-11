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

public class CommandQueue {
    private final String name;
    private final Connection connection;
    private final int maxRetryCount;

    private Channel channel;
    private String queueName;
    private boolean isClosing = false;

    private final List<ICommandHandler> handlers = new ArrayList<>();

    public CommandQueue(String clientName, Connection connection, int maxRetryCount) {
        this.name = clientName + "_command";
        this.connection = connection;
        this.maxRetryCount = maxRetryCount;
    }

    public void connect() {
        try {
            channel = connection.createChannel();

            queueName = channel.queueDeclare(name, false, true, true, null).getQueue();

            {
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        if (isClosing) {
                            return;
                        }

                        var str = new String(body, StandardCharsets.UTF_8);
                        var message = CatMessenger.GSON.fromJson(str, ConnectorCommand.class);

                        try {
                            for (var handler : handlers) {
                                try {
                                    handler.handle(message, properties);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } finally {
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    }
                };

                channel.basicConsume(queueName, false, consumer);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void publish(ConnectorCommand command) {
        var json = CatMessenger.GSON.toJson(command);
        internalPublish(command.getCallback() + "_command", json, 0);
    }

    private void internalPublish(String callback, String message, int retry) {
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

            channel.basicPublish("", callback, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            internalPublish(callback, message, retry + 1);
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
