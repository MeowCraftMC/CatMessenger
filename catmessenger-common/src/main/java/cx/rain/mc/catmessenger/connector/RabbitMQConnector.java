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

    private final Connection connection;
    private final MessageQueue messageQueue;
    private final CommandQueue commandQueue;

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
        commandQueue = new CommandQueue(name, connection, maxRetryCount);
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
        commandQueue.connect();
    }

    public void disconnect() {
        messageQueue.disconnect();
        commandQueue.disconnect();
        try {
            connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public CommandQueue getCommandQueue() {
        return commandQueue;
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

    public void publish(ConnectorCommand command) {
        commandQueue.publish(command);
    }
}
