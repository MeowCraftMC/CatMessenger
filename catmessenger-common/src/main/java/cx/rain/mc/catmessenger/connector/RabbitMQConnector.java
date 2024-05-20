package cx.rain.mc.catmessenger.connector;

import com.rabbitmq.client.*;
import cx.rain.mc.catmessenger.CatMessenger;
import cx.rain.mc.catmessenger.message.AbstractMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitMQConnector {

    private final ConnectionFactory factory;
    private final String name;

    private Connection connection;
    private MessageQueue messageQueue;

    public RabbitMQConnector(String name, int maxRetryCount, String host, int port, String virtualHost, String username, String password) {
        this.name = name;

        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);

        connection = createConnection();
        messageQueue = new MessageQueue(name, connection, maxRetryCount);
    }

    private Connection createConnection() {
        try {
            return factory.newConnection(name);
        } catch (IOException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void connect() {
        messageQueue.connect();
    }

    public void publish(AbstractMessage message) {
        publish(message, null);
    }

    public void publish(AbstractMessage message, ZonedDateTime time) {
        publish(message, null, time);
    }

    public void publish(AbstractMessage message, AbstractMessage sender, ZonedDateTime time) {
        var connectorMessage = new ConnectorMessage();
        connectorMessage.setContent(message);
        connectorMessage.setSender(sender);
        connectorMessage.setTime(time);
        publish(connectorMessage);
    }

    public void publish(ConnectorMessage message) {
        messageQueue.publish(message);
    }

    public void disconnect() {
        messageQueue.disconnect();
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }
}
