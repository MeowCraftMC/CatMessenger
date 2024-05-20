package cx.rain.mc.catmessenger.bukkit;

import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.bukkit.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.bukkit.utility.MessageHelper;
import cx.rain.mc.catmessenger.connector.RabbitMQConnector;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
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

        connector.getMessageQueue().addHandler(message -> Bukkit.getScheduler()
                .scheduleSyncDelayedTask(this, () -> {
                    var components = MessageHelper.toBroadcast(message).toArray(BaseComponent[]::new);
                    Bukkit.spigot().broadcast(components);
                    var component = new ComponentBuilder().append(components).build();
                    logger.info(component.toPlainText());
                }));
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading CatMessenger.");

        getConnector().connect();

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);

        getConnector().publish(MessageHelper.buildServerOnlineMessage());

        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        getConnector().publish(MessageHelper.buildServerOfflineMessage());

        getConnector().disconnect();

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
