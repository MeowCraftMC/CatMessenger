package cx.rain.mc.catmessenger.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import cx.rain.mc.catmessenger.connector.RabbitMQConnector;
import cx.rain.mc.catmessenger.velocity.config.ConfigManager;
import cx.rain.mc.catmessenger.velocity.utility.MessageHelper;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Getter
@Plugin(id = "catmessenger", name = "CatMessenger", version = "2.0.0",
        url = "https://github.com/MeowCraftMC/CatMessenger", authors = {"qyl27"})
public final class CatMessengerVelocity {
    private static CatMessengerVelocity INSTANCE;

    private final Logger logger;

    private final RabbitMQConnector connector;

    @Inject
    public CatMessengerVelocity(Logger logger, @DataDirectory Path dataDirectory) {
        INSTANCE = this;

        this.logger = logger;

        ConfigManager config = new ConfigManager(dataDirectory);

        connector = new RabbitMQConnector(config.get().getId(), config.get().getRabbitMQ().getMaxRetry(),
                config.get().getRabbitMQ().getHost(), config.get().getRabbitMQ().getPort(),
                config.get().getRabbitMQ().getVirtualHost(),
                config.get().getRabbitMQ().getUsername(), config.get().getRabbitMQ().getPassword());

        // Todo: print to logger
    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        getConnector().connect();

        getConnector().publish(MessageHelper.buildServerOnlineMessage());

        getLogger().info("Loaded!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        getConnector().publish(MessageHelper.buildServerOfflineMessage());

        getConnector().disconnect();

        getLogger().info("Bye~");
    }

    public static CatMessengerVelocity getInstance() {
        return INSTANCE;
    }

}
