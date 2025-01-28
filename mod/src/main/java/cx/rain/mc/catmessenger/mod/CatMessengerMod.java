package cx.rain.mc.catmessenger.mod;

import cx.rain.mc.catmessenger.api.CatMessenger;
import cx.rain.mc.catmessenger.api.utilities.MessageHelper;
import cx.rain.mc.catmessenger.api.utilities.MessageParser;
import cx.rain.mc.catmessenger.mod.config.ModConfig;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;

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


        messenger.connect();

//        messenger.consumeMessage(message -> server.execute(() -> {
//                    var components = MessageParser.parseFrom(message);
//                    server.getPlayerList().broadcastSystemMessage(components, false);
//                }));

//        connector.getCommandQueue().addHandler((command, props) -> {
//            if (!command.getCallback().equals(config.name)) {
//                return;
//            }
//
//            switch (command.getCommand()) {
//                case QUERY_ONLINE -> {
//                    var response = new ConnectorCommand();
//                    response.setSender(config.name);
//                    response.setCallback(command.getSender());
//                    response.setReplyTo(command.getReplyTo());
//                    response.setCommand(ConnectorCommand.EnumCommand.RESPONSE_ONLINE);
//                    var playerCount = server.getPlayerCount();
//                    var args = new ArrayList<String>();
//                    args.add(Integer.toString(playerCount));
//                    args.addAll(Arrays.stream(server.getPlayerNames()).toList());
//                    response.setArguments(args.toArray(String[]::new));
//                    connector.getCommandQueue().publish(response);
//                }
//                case QUERY_WORLD_TIME -> {
//                    var response = new ConnectorCommand();
//                    response.setSender(config.name);
//                    response.setCallback(command.getSender());
//                    response.setReplyTo(command.getReplyTo());
//                    response.setCommand(ConnectorCommand.EnumCommand.RESPONSE_ONLINE);
//
//                    var worldName = command.getArguments()[0];
//                    var typeName = command.getArguments()[1];
//                    for (var level : server.getAllLevels()) {
//                        if (level.serverLevelData.getLevelName().equals(worldName)) {
//                            var time = -1L;
//                            if (typeName.equalsIgnoreCase("daytime")) {
//                                time = level.getDayTime();
//                            } else if (typeName.equalsIgnoreCase("gametime")) {
//                                time = level.getGameTime();
//                            } else if (typeName.equalsIgnoreCase("day")) {
//                                time = level.getGameTime() / 24000;
//                            }
//                            response.setArguments(new String[] {Long.toString(time)});
//                            connector.getCommandQueue().publish(response);
//                            return;
//                        }
//                    }
//
//                    response.setArguments(new String[] {"-1"});
//                    connector.getCommandQueue().publish(response);
//                }
//                case ERROR, ONLINE, OFFLINE, RESPONSE_ONLINE, RESPONSE_WORLD_TIME, RUN_COMMAND, COMMAND_RESULT -> {
//                }
//            }
//        });

//        PlayerEvent.PLAYER_JOIN.register(player -> connector.publish(MessageHelper.buildJoinMessage(player.getName().getString())));
//        PlayerEvent.PLAYER_QUIT.register(player -> connector.publish(MessageHelper.buildQuitMessage(player.getName().getString())));

//        PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) ->
//                advancement.value().display().ifPresent(d ->
//                        connector.publish(MessageHelper.buildAdvancementMessage(player.getName().getString(),
//                                d.getTitle().getString(),
//                                d.getDescription().getString(),
//                                d.getType()))));

//        EntityEvent.LIVING_DEATH.register((entity, source) -> {
//            if (entity instanceof Player player) {
//                connector.publish(MessageHelper.buildPlayerDeathMessage(player.getName().getString(), source.getLocalizedDeathMessage(entity)));
//            }
//            return EventResult.pass();
//        });

//        ChatEvent.RECEIVED.register((player, component) -> {
//            if (player != null) {
//                connector.publish(MessageHelper.buildChatMessage(player.getName().getString(), player.getUUID(), component));
//            }
//
//            return EventResult.pass();
//        });

//        connector.connect();
//        connector.publish(MessageHelper.buildServerOnlineMessage());
    }

    public void stop(MinecraftServer server) {
//        connector.publish(MessageHelper.buildServerOfflineMessage());
//        connector.disconnect();
    }
}
