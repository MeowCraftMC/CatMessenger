package cx.rain.mc.catmessenger.bungee;

import cx.rain.mc.catmessenger.bungee.bot.Bot;
import cx.rain.mc.catmessenger.bungee.config.ConfigManager;
import cx.rain.mc.catmessenger.bungee.hadler.PluginMessageHandler;
import cx.rain.mc.catmessenger.bungee.http.OkHttpRetryInterceptor;
import cx.rain.mc.catmessenger.bungee.utility.MessageSendHelper;
import cx.rain.mc.catmessenger.common.CatMessenger;
import net.md_5.bungee.api.plugin.Plugin;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

public final class MessengerBungee extends Plugin {
    private static MessengerBungee INSTANCE;

    private final ConfigManager configManager;

    private Bot bot;

    public MessengerBungee() {
        INSTANCE = this;

        configManager = new ConfigManager(this);

        reloadBot();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getProxy().registerChannel(CatMessenger.MESSAGES_CHANNEL_NAME);

        getProxy().getPluginManager().registerListener(this, new PluginMessageHandler());

        bot.start();

        MessageSendHelper.sendSystemMessage("BungeeCord 启动了！"); // Todo: qyl27: I18n.
        getSLF4JLogger().info("Telegram bot started.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        MessageSendHelper.sendSystemMessage("BungeeCord 关闭了！"); // Todo: qyl27: I18n.

        bot.stop();
        getSLF4JLogger().info("Telegram bot stopped.");
    }

    public static MessengerBungee getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Bot getBot() {
        return bot;
    }

    public void reloadBot() {
        if (bot != null) {
            bot.stop();
        }

        var okHttpBuilder = new OkHttpClient().newBuilder()
                .readTimeout(Duration.ZERO)
                .addInterceptor(new OkHttpRetryInterceptor(5));

        if (configManager.hasProxy()) {
            okHttpBuilder.setProxy$okhttp(new Proxy(configManager.getProxyType(), new InetSocketAddress(configManager.getProxyHost(), configManager.getProxyPort())));
        }

        bot = new Bot(configManager.getTelegramToken(), okHttpBuilder.build());
    }
}
