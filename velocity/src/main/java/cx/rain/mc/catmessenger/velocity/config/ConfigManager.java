package cx.rain.mc.catmessenger.velocity.config;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.io.File;
import java.nio.file.Path;

public class ConfigManager {

    private final File configFile;
    private final HoconConfigurationLoader loader;

    private ConfigurationNode rootNode;

    @SneakyThrows
    public ConfigManager(Path dataDir) {
        configFile = new File(dataDir.toFile(), "catmessenger.conf");
        loader = HoconConfigurationLoader.builder()
                .path(configFile.toPath())
                .emitComments(true)
                .prettyPrinting(true)
                .build();

        reload();
    }

    @SneakyThrows
    private void saveDefault() {
        var root = loader.createNode();
        root.set(new Config());
        loader.save(root);
    }

    @SneakyThrows
    public void reload() {
        if (!configFile.exists()) {
            saveDefault();
        }
        rootNode = loader.load();
    }

    @SneakyThrows
    public Config get() {
        return rootNode.get(Config.class);
    }

    @Getter
    @Setter
    @ConfigSerializable
    public static class Config {
        @Comment("Client Id.")
        private String id = "velocity";

        @Comment("Platform name.")
        private String name = "ExampleVelocity";

        @Comment("RabbitMQ connection info.")
        private RabbitMQConfig rabbitMQ = new RabbitMQConfig();

        @Getter
        @Setter
        @ConfigSerializable
        public static class RabbitMQConfig {
            private String host = "localhost";

            private int port = 5672;

            private String username = "guest";

            private String password = "guest";

            private String virtualHost = "/minecraft";
        }
    }
}
