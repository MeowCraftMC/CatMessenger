package cx.rain.mc.catmessenger.matrix.config;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

public class ConfigManager {
    private final Path dataDir;

    private final YamlConfigurationLoader config;
    private ConfigurationNode rootNode;

    public ConfigManager(Path dataDir) {
        this.dataDir = dataDir;

        var file = new File(dataDir.toFile(), "catmessenger.conf");

        config = YamlConfigurationLoader.builder()
                .file(file)
                .indent(2)
                .build();

        try {
            if (!file.exists()) {
                saveDefault();
            }

            rootNode = config.load();
        } catch (ConfigurateException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void saveDefault() throws ConfigurateException {
        var root = config.createNode();
        root.commentIfAbsent("Config of CatMessenger.");

        var name = config.createNode();
        name.commentIfAbsent("Server name.");
        name.set("ExampleVelocityServer");
        root.childrenMap().put("name", name);

        var connector = config.createNode();
        connector.commentIfAbsent("RabbitMQ connection info.");
        var connectorHost = config.createNode();
        var connectorPort = config.createNode();
        var connectorVirtualHost = config.createNode();
        var connectorUsername = config.createNode();
        var connectorPassword = config.createNode();
        var connectorRetry = config.createNode();
        connectorHost.set("localhost");
        connectorPort.set(5672);
        connectorVirtualHost.set("/minecraft");
        connectorUsername.set("guest");
        connectorPassword.set("guest");
        connectorRetry.set(5);
        connector.childrenMap().put("host", connectorHost);
        connector.childrenMap().put("port", connectorPort);
        connector.childrenMap().put("virtualHost", connectorVirtualHost);
        connector.childrenMap().put("username", connectorUsername);
        connector.childrenMap().put("password", connectorPassword);
        connector.childrenMap().put("maxRetry", connectorRetry);
        root.childrenMap().put("connector", connector);

        var matrix = config.createNode();
        matrix.commentIfAbsent("Matrix client settings.");
        var matrixHomeServer = config.createNode();
        var matrixUsername = config.createNode();
        var matrixPassword = config.createNode();
        var matrixRoom = config.createNode();
        matrixHomeServer.set("matrix.org");
        matrixUsername.set("example");
        matrixPassword.set("");
        matrixRoom.set("!roomId:matrix.org");
        matrix.childrenMap().put("homeServer", matrixHomeServer);
        matrix.childrenMap().put("username", matrixUsername);
        matrix.childrenMap().put("password", matrixPassword);
        matrix.childrenMap().put("room", matrixRoom);
        root.childrenMap().put("matrix", matrix);

        config.save(root);
    }

    public String getName() {
        return rootNode.node("name").getString();
    }

    public String getConnectorHost() {
        return rootNode.node("connector", "host").getString();
    }

    public int getConnectorPort() {
        return rootNode.node("connector", "port").getInt();
    }

    public String getConnectorVirtualHost() {
        return rootNode.node("connector", "virtualHost").getString();
    }

    public String getConnectorUsername() {
        return rootNode.node("connector", "username").getString();
    }

    public String getConnectorPassword() {
        return rootNode.node("connector", "password").getString();
    }

    public int getConnectorRetry() {
        return rootNode.node("connector", "maxRetry").getInt();
    }

    public String getMatrixHomeServer() {
        return rootNode.node("matrix", "homeServer").getString();
    }

    public String getMatrixUsername() {
        return rootNode.node("matrix", "username").getString();
    }

    public String getMatrixPassword() {
        return rootNode.node("matrix", "password").getString();
    }

    public String getMatrixRoom() {
        return rootNode.node("matrix", "room").getString();
    }
}
