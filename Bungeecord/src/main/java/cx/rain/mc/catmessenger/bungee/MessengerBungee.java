package cx.rain.mc.catmessenger.bungee;

import cx.rain.mc.catmessenger.bungee.config.ConfigManager;
import cx.rain.mc.catmessenger.common.CatMessenger;
import net.md_5.bungee.api.plugin.Plugin;

public final class MessengerBungee extends Plugin {
    private static MessengerBungee INSTANCE;

    private final ConfigManager configManager;

    public MessengerBungee() {
        INSTANCE = this;

        configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getProxy().registerChannel(CatMessenger.MESSAGES_CHANNEL_NAME);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MessengerBungee getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
