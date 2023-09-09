package cx.rain.mc.catmessenger.bungee.config;

import cx.rain.mc.catmessenger.bungee.MessengerBungee;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Files;

public class ConfigManager {
    private final MessengerBungee plugin;
    private final Logger logger;

    private final File configFile;
    private final Configuration config;

    public ConfigManager(MessengerBungee plugin) {
        this.plugin = plugin;

        logger = plugin.getSLF4JLogger();

        configFile = new File(plugin.getDataFolder(), "config.yml");
        saveDefault();

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException ex) {
            logger.warn("Cannot read configuration file.", ex);
            throw new RuntimeException(ex);
        }
    }

    private void saveDefault() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!configFile.exists()) {
            try (var is = plugin.getResourceAsStream("config.yml")) {
                Files.copy(is, configFile.toPath());
            } catch (IOException ex) {
                logger.warn("Cannot create default configuration file.", ex);
            }
        }
    }

    public String getTelegramToken() {
        return config.getString("telegram.token");
    }

    public String getGroupId() {
        return config.getString("telegram.group_id");
    }

    public boolean hasProxy() {
        return getProxyType() == Proxy.Type.DIRECT;
    }

    public Proxy.Type getProxyType() {
        var type = config.getString("proxy.type");

        if (type.equalsIgnoreCase("socks")) {
            return Proxy.Type.SOCKS;
        } else if (type.equalsIgnoreCase("http")) {
            return Proxy.Type.HTTP;
        }

        return Proxy.Type.DIRECT;
    }

    public String getProxyHost() {
        return config.getString("proxy.host");
    }

    public int getProxyPort() {
        return config.getInt("proxy.port");
    }

    public String getMessageFormat() {
        return config.getString("format.message");
    }

    public String getSystemMessageFormat() {
        return config.getString("format.system");
    }
}
