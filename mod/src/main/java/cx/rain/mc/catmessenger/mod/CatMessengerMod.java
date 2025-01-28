package cx.rain.mc.catmessenger.mod;

import cx.rain.mc.catmessenger.api.CatMessenger;
import cx.rain.mc.catmessenger.api.message.Message;
import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
import cx.rain.mc.catmessenger.api.utilities.MessageFactory;
import cx.rain.mc.catmessenger.api.utilities.MessageParser;
import cx.rain.mc.catmessenger.mod.config.ModConfig;
import cx.rain.mc.catmessenger.mod.utility.ModMessengerHelper;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.kyori.adventure.audience.Audience;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

public class CatMessengerMod {

    private final ModConfig config;
    private final CatMessenger messenger;

    public CatMessengerMod(ModConfig config) {
        this.config = config;

        this.messenger = new CatMessenger(config.getId(),
                config.getRabbitMQ().getHost(), config.getRabbitMQ().getPort(),
                config.getRabbitMQ().getUsername(), config.getRabbitMQ().getPassword(),
                config.getRabbitMQ().getVirtualHost(),
                config.getRabbitMQ().getMaxRetry(), config.getRabbitMQ().getRetryIntervalMillis());
    }

    public void start(MinecraftServer server) {
        messenger.consumeMessage(message -> server.execute(() -> {
            var components = MessageParser.parseFrom(message);
            server.getPlayerList().getPlayers().forEach(player -> {
                if (player instanceof Audience audience) {
                    audience.sendMessage(components);
                }
            });
            if (server instanceof Audience audience) {
                audience.sendMessage(components);
            }
        }));


        PlayerEvent.PLAYER_JOIN.register(player -> sendMessage(MessageFactory.playerJoined(ModMessengerHelper.createPlayer(player))));
        PlayerEvent.PLAYER_QUIT.register(player -> sendMessage(MessageFactory.playerLeft(ModMessengerHelper.createPlayer(player))));

        PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) ->
                advancement.value().display().ifPresent(d ->
                        sendMessage(ModMessengerHelper.playerAdvancement(player, d))));

        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof Player player) {
                sendMessage(source.getLocalizedDeathMessage(player));
            }

            return EventResult.pass();
        });

        ChatEvent.RECEIVED.register((player, component) -> {
            if (player != null) {
                sendMessage(player, component);
            }

            return EventResult.pass();
        });

        messenger.connect();

        sendMessage(MessageFactory.serverOnline());
    }

    public void stop(MinecraftServer server) {
        sendMessage(MessageFactory.serverOffline());

        messenger.disconnect();
    }

    public void sendMessage(Component component) {
        var aComponent = ModMessengerHelper.toComponent(component);
        sendMessage(aComponent);
    }

    public void sendMessage(Player player, Component component) {
        var aComponent = ModMessengerHelper.toComponent(component);
        sendMessage(ModMessengerHelper.createPlayer(player), aComponent);
    }

    public void sendMessage(net.kyori.adventure.text.Component component) {
        var content = ComponentSerializer.toMiniMessage(component);
        messenger.sendMessage(new Message(config.getName(), content));
    }

    public void sendMessage(cx.rain.mc.catmessenger.api.message.Player player, net.kyori.adventure.text.Component component) {
        var content = ComponentSerializer.toMiniMessage(component);
        messenger.sendMessage(new Message(config.getName(), player, content));
    }
}
