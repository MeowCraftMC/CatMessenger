package cx.rain.mc.catmessenger.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import cx.rain.mc.catmessenger.api.CatMessenger;
import cx.rain.mc.catmessenger.api.model.Message;
import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
import cx.rain.mc.catmessenger.api.utilities.MessageFactory;
import cx.rain.mc.catmessenger.api.utilities.ComponentParser;
import cx.rain.mc.catmessenger.velocity.config.ConfigManager;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "catmessenger", name = "CatMessenger", version = "3.0.0",
        url = "https://github.com/MeowCraftMC/CatMessenger", authors = {"qyl27"})
public final class CatMessengerVelocity {

    private final Logger logger;
    private final ConfigManager config;
    private final CatMessenger messenger;

    @Inject
    public CatMessengerVelocity(Logger logger, @DataDirectory Path dataDirectory) {
        this.logger = logger;

        this.config = new ConfigManager(dataDirectory);

        this.messenger = new CatMessenger(config.get().getId(),
                config.get().getRabbitMQ().getHost(), config.get().getRabbitMQ().getPort(),
                config.get().getRabbitMQ().getUsername(), config.get().getRabbitMQ().getPassword(),
                config.get().getRabbitMQ().getVirtualHost());

        this.messenger.getMessage().handler(message -> {
            var component = ComponentParser.parseFrom(message);
            logger.info(ComponentSerializer.toPlain(component));
        });
    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        this.messenger.connect();

        sendMessage(MessageFactory.serverOnline(true));
        logger.info("Loaded!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        sendMessage(MessageFactory.serverOffline(true));

        this.messenger.disconnect();
        logger.info("Bye~");
    }

    public void sendMessage(Component component) {
        logger.info(ComponentSerializer.toPlain(component));
        var content = ComponentSerializer.toJson(component);
        this.messenger.getMessage().publish(new Message(config.get().getName(), content));
    }
}
