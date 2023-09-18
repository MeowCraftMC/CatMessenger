package cx.rain.mc.catmessenger.bukkit;

import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.bukkit.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.bukkit.networking.ConnectorClient;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URISyntaxException;

public final class MessengerBukkit extends JavaPlugin {
    private static MessengerBukkit INSTANCE;

    private final ConfigManager configManager;
    private final ConnectorClient connectorClient;

    public MessengerBukkit() throws URISyntaxException {
        INSTANCE = this;

        configManager = new ConfigManager(this);
        connectorClient = new ConnectorClient(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading CatMessenger.");

//        getLogger().info("Loading CatMessenger. Please ensure your server is running on bungee mode.");
//
//        // We believe it is running on bungee mode.
//        getServer().getMessenger().registerIncomingPluginChannel(this, CatMessenger.MESSAGES_CHANNEL_NAME, new ChatPluginMessageListener(this));
//        getServer().getMessenger().registerOutgoingPluginChannel(this, CatMessenger.MESSAGES_CHANNEL_NAME);

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);

        connectorClient.connect();

//        MessageSender.sendSystemMessage(getServer(), "服务器 " + configManager.getServerName() + " 启动了！");
        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        connectorClient.close();
//        MessageSender.sendSystemMessage(getServer(), "服务器 " + configManager.getServerName() + " 关闭了！");
    }

    public static MessengerBukkit getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ConnectorClient getConnectorClient() {
        return connectorClient;
    }
}
