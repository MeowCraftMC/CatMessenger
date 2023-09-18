package cx.rain.mc.catmessenger.bukkit.config;

import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.net.URI;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(MessengerBukkit plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();
    }

    public String getServerName() {
        return config.getString("server_name");
    }

    public boolean broadcastSystemMessage() {
        return config.getBoolean("broadcast_system_message");
    }

    public boolean showSystemMessage() {
        return config.getBoolean("show_system_message");
    }

    public String getRemoteConnector() {
        return config.getString("connector.url");
    }

    public String getConnectorSecret() {
        return config.getString("connector.secret");
    }
}
