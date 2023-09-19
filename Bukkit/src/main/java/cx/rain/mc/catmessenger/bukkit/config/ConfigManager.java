package cx.rain.mc.catmessenger.bukkit.config;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(CatMessengerBukkit plugin) {
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

    public String getFormatRaw() {
        return config.getString("formats.raw");
    }

    public String getFormatChat() {
        return config.getString("formats.chat_text");
    }

    public String getFormatSystem() {
        return config.getString("formats.system");
    }

    public String getFormatPlayerOnline() {
        return config.getString("formats.player_online");
    }

    public String getFormatPlayerOffline() {
        return config.getString("formats.player_offline");
    }

    public String getFormatServerOnline() {
        return config.getString("formats.server_online");
    }

    public String getFormatServerOffline() {
        return config.getString("formats.server_offline");
    }
}
