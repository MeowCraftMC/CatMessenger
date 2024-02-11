package cx.rain.mc.catmessenger.matrix;

import com.cosium.matrix_communication_client.MatrixResources;
import cx.rain.mc.catmessenger.connector.RabbitMQConnector;
import cx.rain.mc.catmessenger.matrix.config.ConfigManager;

import java.nio.file.Path;

public final class CatMessengerMatrix {
    private static CatMessengerMatrix INSTANCE;

    private final ConfigManager config;
    private final RabbitMQConnector connector;

    public CatMessengerMatrix() {
        INSTANCE = this;

        config = new ConfigManager(Path.of("data"));

        connector = new RabbitMQConnector(config.getName(), config.getConnectorRetry(),
                config.getConnectorHost(), config.getConnectorPort(), config.getConnectorVirtualHost(),
                config.getConnectorUsername(), config.getConnectorPassword());

        var matrix = MatrixResources.factory()
                .builder()
                .https()
                .hostname(config.getMatrixHomeServer())
                .defaultPort()
                .usernamePassword(config.getMatrixUsername(), config.getMatrixPassword())
                .build();

        var room = matrix.rooms().byId(config.getMatrixRoom());

        connector.addHandler((message, sender) -> {

        });
    }

    public static CatMessengerMatrix getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfig() {
        return config;
    }

    public void start() {
        connector.connect();
    }

    public void stop() {
        connector.disconnect();
    }
}
