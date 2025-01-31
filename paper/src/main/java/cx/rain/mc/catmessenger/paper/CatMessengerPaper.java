package cx.rain.mc.catmessenger.paper;

import cx.rain.mc.catmessenger.api.CatMessenger;
import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
import cx.rain.mc.catmessenger.api.utilities.MessageFactory;
import cx.rain.mc.catmessenger.api.utilities.ComponentParser;
import cx.rain.mc.catmessenger.paper.config.ConfigManager;
import cx.rain.mc.catmessenger.paper.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.paper.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.paper.utility.MessengerHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CatMessengerPaper extends JavaPlugin {
    private static CatMessengerPaper INSTANCE;

    private final ConfigManager configManager = new ConfigManager(this);

    private final CatMessenger messenger;

    public CatMessengerPaper() {
        INSTANCE = this;

        this.messenger = new CatMessenger(configManager.getId(),
                configManager.getRabbitMQHost(), configManager.getRabbitMQPort(),
                configManager.getRabbitMQUsername(), configManager.getRabbitMQPassword(),
                configManager.getRabbitMQVirtualHost());

        this.messenger.getMessage().handler(message -> Bukkit.getScheduler()
                .scheduleSyncDelayedTask(CatMessengerPaper.getInstance(), () -> {
                    var component = ComponentParser.parseFrom(message);
                    Bukkit.broadcast(component);
                }));
    }

    @Override
    public void onEnable() {
        this.messenger.connect();

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);

        MessengerHelper.send(MessageFactory.serverOnline());
        getSLF4JLogger().info("CatMessenger loaded.");
    }

    @Override
    public void onDisable() {
        MessengerHelper.send(MessageFactory.serverOffline());
        this.messenger.disconnect();

        getSLF4JLogger().info("CatMessenger unloaded.");
    }

    public static CatMessengerPaper getInstance() {
        return INSTANCE;
    }
}
