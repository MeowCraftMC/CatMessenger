package cx.rain.mc.catmessenger.paper.config;

import cx.rain.mc.catmessenger.paper.CatMessengerPaper;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(CatMessengerPaper plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();
    }

    public String getId() {
        return config.getString("id");
    }

    public String getName() {
        return config.getString("name");
    }

    public String getRabbitMQHost() {
        return config.getString("rabbitmq.host");
    }

    public int getRabbitMQPort() {
        return config.getInt("rabbitmq.port");
    }

    public String getRabbitMQVirtualHost() {
        return config.getString("rabbitmq.virtualHost");
    }

    public String getRabbitMQUsername() {
        return config.getString("rabbitmq.username");
    }

    public String getRabbitMQPassword() {
        return config.getString("rabbitmq.password");
    }

    public int getRabbitMQMaxRetry() {
        return config.getInt("rabbitmq.maxRetry");
    }

    public long getRabbitMQRetryInterval() {
        return config.getLong("rabbitmq.retryIntervalMillis");
    }
}
