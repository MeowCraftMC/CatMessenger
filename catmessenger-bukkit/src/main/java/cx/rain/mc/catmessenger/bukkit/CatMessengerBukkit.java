package cx.rain.mc.catmessenger.bukkit;

import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.bukkit.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.bukkit.utility.MessageHelper;
import cx.rain.mc.catmessenger.connector.RabbitMQConnector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CatMessengerBukkit extends JavaPlugin {
    private static CatMessengerBukkit INSTANCE;

    private final Logger logger = getLogger();
    private final ConfigManager config = new ConfigManager(this);
    private final RabbitMQConnector connector;

    public CatMessengerBukkit() {
        INSTANCE = this;

        connector = new RabbitMQConnector(config.getName(), config.getConnectorRetry(),
                config.getConnectorHost(), config.getConnectorPort(), config.getConnectorVirtualHost(),
                config.getConnectorUsername(), config.getConnectorPassword());

        connector.addHandler(((message, sender) -> Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            var component = MessageHelper.toBroadcast(sender, message);
            Bukkit.spigot().broadcast(component);
            logger.info(component.toPlainText());
        })));
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading CatMessenger.");

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);

        getConnector().publish(MessageHelper.buildServerOnlineMessage());

        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getConnector().publish(MessageHelper.buildServerOfflineMessage());
        getLogger().info("Bye~");
    }

    public static CatMessengerBukkit getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return config;
    }

    public RabbitMQConnector getConnector() {
        return connector;
    }
}