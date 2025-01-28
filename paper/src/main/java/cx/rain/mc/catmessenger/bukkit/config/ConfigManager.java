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

    public String getBrokerHost() {
        return config.getString("broker.host");
    }

    public int getBrokerPort() {
        return config.getInt("broker.port");
    }

    public int getBrokerTimeout() {
        return config.getInt("broker.timeoutMillis");
    }
}
