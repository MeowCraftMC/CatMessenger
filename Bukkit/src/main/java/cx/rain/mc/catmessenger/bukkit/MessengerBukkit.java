package cx.rain.mc.catmessenger.bukkit;

import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.bukkit.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.bukkit.socket.SocketIOClientManager;
import cx.rain.mc.catmessenger.bukkit.utility.MessageSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class MessengerBukkit extends JavaPlugin {
    private static MessengerBukkit INSTANCE;

    private final ConfigManager configManager;
    private final SocketIOClientManager socketIOClientManager;

    public MessengerBukkit() {
        INSTANCE = this;

        configManager = new ConfigManager(this);
        socketIOClientManager = new SocketIOClientManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading CatMessenger. Please ensure your server is running behind a BungeeCord server.");

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);

        MessageSender.sendSystemMessage("服务器 " + configManager.getServerName() + " 启动了！");
        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MessageSender.sendSystemMessage("服务器 " + configManager.getServerName() + " 关闭了！");
    }

    public static MessengerBukkit getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public SocketIOClientManager getSocketIoClientManager() {
        return socketIOClientManager;
    }
}
