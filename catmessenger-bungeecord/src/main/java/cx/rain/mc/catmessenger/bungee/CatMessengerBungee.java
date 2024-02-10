package cx.rain.mc.catmessenger.bungee;

import cx.rain.mc.catmessenger.bungee.config.ConfigManager;
import cx.rain.mc.catmessenger.bungee.utility.MessageHelper;
import cx.rain.mc.catmessenger.connector.RabbitMQConnector;
import net.md_5.bungee.api.plugin.Plugin;

public final class CatMessengerBungee extends Plugin {
    private static CatMessengerBungee INSTANCE;

    private final ConfigManager config;
    private final RabbitMQConnector connector;

    public CatMessengerBungee() {
        INSTANCE = this;

        config = new ConfigManager(this);

        connector = new RabbitMQConnector(config.getName(), config.getConnectorRetry(),
                config.getConnectorHost(), config.getConnectorPort(), config.getConnectorVirtualHost(),
                config.getConnectorUsername(), config.getConnectorPassword());
    }

    @Override
    public void onEnable() {
        getConnector().publish(MessageHelper.buildServerOnlineMessage());

        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        getConnector().publish(MessageHelper.buildServerOfflineMessage());

        getLogger().info("Bye~");
    }

    public static CatMessengerBungee getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return config;
    }

    public RabbitMQConnector getConnector() {
        return connector;
    }
}
