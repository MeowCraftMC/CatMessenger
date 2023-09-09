package cx.rain.mc.catmessenger.bukkit;

import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.bukkit.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.bukkit.networking.ChatPluginMessageListener;
import cx.rain.mc.catmessenger.bukkit.utility.MessageSender;
import cx.rain.mc.catmessenger.common.CatMessenger;
import org.bukkit.plugin.java.JavaPlugin;

public final class MessengerBukkit extends JavaPlugin {
    private static MessengerBukkit INSTANCE;

    private final ConfigManager configManager;

    public MessengerBukkit() {
        INSTANCE = this;

        configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading CatMessenger. Please ensure your server is running on bungee mode.");

        // We believe it is running on bungee mode.
        getServer().getMessenger().registerIncomingPluginChannel(this, CatMessenger.MESSAGES_CHANNEL_NAME, new ChatPluginMessageListener(this));
        getServer().getMessenger().registerOutgoingPluginChannel(this, CatMessenger.MESSAGES_CHANNEL_NAME);

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);

        MessageSender.sendSystemMessage(getServer(), "服务器 " + configManager.getServerName() + " 启动了！");
        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
//        MessageSender.sendSystemMessage(getServer(), "服务器 " + configManager.getServerName() + " 关闭了！");
    }

    public static MessengerBukkit getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
