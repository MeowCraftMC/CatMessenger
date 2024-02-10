package cx.rain.mc.catmessenger.bungee.config;

import cx.rain.mc.catmessenger.bungee.CatMessengerBungee;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigManager {
    private final CatMessengerBungee plugin;
    private final Logger logger;

    private final File configFile;
    private final Configuration config;

    public ConfigManager(CatMessengerBungee plugin) {
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
