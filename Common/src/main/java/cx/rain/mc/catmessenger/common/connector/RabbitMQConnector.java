package cx.rain.mc.catmessenger.common.connector;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConnector {
    private final ConnectionFactory factory;

    private final String name;

    private Connection connection;
    private Channel channel;

    public RabbitMQConnector(String name, String host, int port, String virtualHost, String username, String password) {
        factory = new ConnectionFactory();

        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);

        this.name = name;
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

    public void connect() {
        connection = createConnection();
        channel = createChannel();
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
