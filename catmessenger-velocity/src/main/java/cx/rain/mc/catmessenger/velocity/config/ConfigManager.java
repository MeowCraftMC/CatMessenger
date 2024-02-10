package cx.rain.mc.catmessenger.velocity.config;

import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

public class ConfigManager {
    private final Logger logger;
    private final Path dataDir;

    private final HoconConfigurationLoader config;
    private ConfigurationNode rootNode;

    public ConfigManager(Logger logger, Path dataDir) {
        this.logger = logger;
        this.dataDir = dataDir;

        var file = new File(dataDir.toFile(), "catmessenger.conf");

        config = HoconConfigurationLoader.builder()
                .file(file)
                .emitComments(true)
                .prettyPrinting(true)
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
        root.childrenMap().put("name", name);
        name.commentIfAbsent("Server name.");
        name.set("ExampleVelocityServer");
        var connector = config.createNode();
        root.childrenMap().put("connector", connector);
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
}
