package cx.rain.mc.catmessenger.bukkit;

import cx.rain.mc.catmessenger.api.utilities.MessageHelper;
import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.bukkit.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.bukkit.utility.BrokerHelper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class CatMessengerBukkit extends JavaPlugin {
    private static CatMessengerBukkit INSTANCE;

    private final ConfigManager config = new ConfigManager(this);

//    private final Vertx vertx;

    public CatMessengerBukkit() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);

        BrokerHelper.send(MessageHelper.serverOnline());
        getSLF4JLogger().info("CatMessenger loaded.");
    }

    @Override
    public void onDisable() {
        BrokerHelper.send(MessageHelper.serverOffline());
        getSLF4JLogger().info("CatMessenger unloaded.");
    }

    public static CatMessengerBukkit getInstance() {
        return INSTANCE;
    }

    public @NotNull ConfigManager getConfigManager() {
        return config;
    }
}
