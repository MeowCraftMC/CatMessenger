package cx.rain.mc.catmessenger.bukkit;

import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.bukkit.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.bukkit.networking.ConnectorClient;
import cx.rain.mc.catmessenger.bukkit.networking.payload.ServerLifecyclePayload;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URISyntaxException;

public final class CatMessengerBukkit extends JavaPlugin {
    private static CatMessengerBukkit INSTANCE;

    private final ConfigManager configManager;
    private final ConnectorClient connectorClient;

    public CatMessengerBukkit() throws URISyntaxException {
        INSTANCE = this;

        configManager = new ConfigManager(this);
        connectorClient = new ConnectorClient(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading CatMessenger.");

        connectorClient.connect();

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);

        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        connectorClient.send(new ServerLifecyclePayload(false));

        getLogger().info("Bye~");
        connectorClient.close();
    }

    public static CatMessengerBukkit getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ConnectorClient getConnectorClient() {
        return connectorClient;
    }
}
