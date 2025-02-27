package cx.rain.mc.catmessenger.api;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import cx.rain.mc.catmessenger.api.messaging.AbstractNotify;
import cx.rain.mc.catmessenger.api.model.Message;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatMessenger {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatMessenger.class);

    private final String clientId;
    private final ConnectionFactory factory;

    @Getter
    private Connection connection;

    @Getter
    private final AbstractNotify<Message> message;

    public CatMessenger(String clientId,
                        String host, int port, String username, String password, String virtualHost) {
        this.clientId = clientId;

        this.factory = new ConnectionFactory();
        this.factory.setHost(host);
        this.factory.setPort(port);
        this.factory.setVirtualHost(virtualHost);
        this.factory.setUsername(username);
        this.factory.setPassword(password);
        this.factory.setAutomaticRecoveryEnabled(true);

        this.message = new AbstractNotify<>(clientId, this::getConnection, Message.class) {
            @Override
            protected String getExchangeName() {
                return "fanout.exchange.messages";
            }

            @Override
            protected String getQueueName() {
                return "fanout.queue." + clientId;
            }
        };
    }

    @SneakyThrows
    public void connect() {
        connection = factory.newConnection();
        message.connect();
    }

    public void disconnect() {
        message.disconnect();
    }
}
