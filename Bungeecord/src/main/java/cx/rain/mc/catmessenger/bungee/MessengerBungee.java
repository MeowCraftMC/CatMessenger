package cx.rain.mc.catmessenger.bungee;

import cx.rain.mc.catmessenger.bungee.config.ConfigManager;
import cx.rain.mc.catmessenger.bungee.utility.MessageSendHelper;
import cx.rain.mc.catmessenger.common.Constants;
import net.md_5.bungee.api.plugin.Plugin;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

public final class MessengerBungee extends Plugin {
    private static MessengerBungee INSTANCE;

    private final ConfigManager configManager;

    // Todo: qyl27: do we need bungee?
    @Deprecated
    public MessengerBungee() {
        INSTANCE = this;

        configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getProxy().registerChannel(Constants.CHANNEL_ID);

        getSLF4JLogger().error("Indev now!");

        MessageSendHelper.sendSystemMessage("BungeeCord 启动了！"); // Todo: qyl27: I18n.
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        MessageSendHelper.sendSystemMessage("BungeeCord 关闭了！"); // Todo: qyl27: I18n.

        getSLF4JLogger().info("Telegram bot stopped.");
    }

    public static MessengerBungee getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
