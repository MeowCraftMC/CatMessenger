package cx.rain.mc.catmessenger.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import cx.rain.mc.catmessenger.connector.RabbitMQConnector;
import cx.rain.mc.catmessenger.velocity.config.ConfigManager;
import cx.rain.mc.catmessenger.velocity.utility.MessageHelper;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "catmessenger", name = "CatMessenger", version = "2.0.0",
        url = "https://github.com/MeowCraftMC/CatMessenger", authors = {"qyl27"})
public final class CatMessengerVelocity {
    private static CatMessengerVelocity INSTANCE;

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDir;

    private final ConfigManager config;
    private final RabbitMQConnector connector;

    @Inject
    public CatMessengerVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        INSTANCE = this;

        this.server = server;
        this.logger = logger;
        this.dataDir = dataDirectory;

        config = new ConfigManager(logger, dataDirectory);

        connector = new RabbitMQConnector(config.getName(), config.getConnectorRetry(),
                config.getConnectorHost(), config.getConnectorPort(), config.getConnectorVirtualHost(),
                config.getConnectorUsername(), config.getConnectorPassword());
    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        getConnector().publish(MessageHelper.buildServerOnlineMessage());

        getLogger().info("Loaded!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        getConnector().publish(MessageHelper.buildServerOfflineMessage());

        getLogger().info("Bye~");
    }

    public static CatMessengerVelocity getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return logger;
    }

    public RabbitMQConnector getConnector() {
        return connector;
    }
}
