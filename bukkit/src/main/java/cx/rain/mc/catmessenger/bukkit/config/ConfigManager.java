package cx.rain.mc.catmessenger.bukkit.config;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(CatMessengerBukkit plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();
    }

    public String getId() {
        return config.getString("id");
    }

    public String getName() {
        return config.getString("name");
    }

    public String getConnectorHost() {
        return config.getString("connector.host");
    }

    public int getConnectorPort() {
        return config.getInt("connector.port");
    }

    public String getConnectorVirtualHost() {
        return config.getString("connector.virtualHost");
    }

    public String getConnectorUsername() {
        return config.getString("connector.username");
    }

    public String getConnectorPassword() {
        return config.getString("connector.password");
    }

    public int getConnectorRetry() {
        return config.getInt("connector.maxRetry");
    }
}
