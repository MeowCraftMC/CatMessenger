package cx.rain.mc.catmessenger.mod;

import cx.rain.mc.catmessenger.connector.ConnectorCommand;
import cx.rain.mc.catmessenger.connector.RabbitMQConnector;
import cx.rain.mc.catmessenger.mod.config.ModConfig;
import cx.rain.mc.catmessenger.mod.utility.MessageHelper;
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
    private final RabbitMQConnector connector;

    public CatMessengerMod(ModConfig config) {
        this.config = config;

        connector = new RabbitMQConnector(config.name, config.connector.maxRetry,
                config.connector.host, config.connector.port, config.connector.virtualHost,
                config.connector.username, config.connector.password);
    }

    public void start(MinecraftServer server) {
        connector.getMessageQueue().addHandler(message -> server.execute(() -> {
                    var components = MessageHelper.toBroadcast(message);
                    server.sendSystemMessage(components);
                }));

        connector.getCommandQueue().addHandler((command, props) -> {
            if (!command.getCallback().equals(config.name)) {
                return;
            }

            switch (command.getCommand()) {
                case QUERY_ONLINE -> {
                    var response = new ConnectorCommand();
                    response.setSender(config.name);
                    response.setCallback(command.getSender());
                    response.setReplyTo(command.getReplyTo());
                    response.setCommand(ConnectorCommand.EnumCommand.RESPONSE_ONLINE);
                    var playerCount = server.getPlayerCount();
                    var args = new ArrayList<String>();
                    args.add(Integer.toString(playerCount));
                    args.addAll(Arrays.stream(server.getPlayerNames()).toList());
                    response.setArguments(args.toArray(String[]::new));
                    connector.getCommandQueue().publish(response);
                }
                case QUERY_WORLD_TIME -> {
                    var response = new ConnectorCommand();
                    response.setSender(config.name);
                    response.setCallback(command.getSender());
                    response.setReplyTo(command.getReplyTo());
                    response.setCommand(ConnectorCommand.EnumCommand.RESPONSE_ONLINE);

                    var worldName = command.getArguments()[0];
                    var typeName = command.getArguments()[1];
                    for (var level : server.getAllLevels()) {
                        if (level.serverLevelData.getLevelName().equals(worldName)) {
                            var time = -1L;
                            if (typeName.equalsIgnoreCase("daytime")) {
                                time = level.getDayTime();
                            } else if (typeName.equalsIgnoreCase("gametime")) {
                                time = level.getGameTime();
                            } else if (typeName.equalsIgnoreCase("day")) {
                                time = level.getGameTime() / 24000;
                            }
                            response.setArguments(new String[] {Long.toString(time)});
                            connector.getCommandQueue().publish(response);
                            return;
                        }
                    }

                    response.setArguments(new String[] {"-1"});
                    connector.getCommandQueue().publish(response);
                }
                case ERROR, ONLINE, OFFLINE, RESPONSE_ONLINE, RESPONSE_WORLD_TIME, RUN_COMMAND, COMMAND_RESULT -> {
                }
            }
        });

        PlayerEvent.PLAYER_JOIN.register(player -> connector.publish(MessageHelper.buildJoinMessage(player.getName().getString())));
        PlayerEvent.PLAYER_QUIT.register(player -> connector.publish(MessageHelper.buildQuitMessage(player.getName().getString())));
        PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) -> {
            var display = advancement.value().display().orElseThrow();
            connector.publish(MessageHelper
                    .buildAdvancementMessage(player.getName().getString(),
                            display.getTitle().getString(),
                            display.getDescription().getString(),
                            display.getType()));
        });

        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof Player player) {
                connector.publish(MessageHelper.buildPlayerDeathMessage(player.getName().getString(), source.getLocalizedDeathMessage(entity)));
            }
            return EventResult.pass();
        });

        ChatEvent.RECEIVED.register((player, component) -> {
            connector.publish(MessageHelper.buildChatMessage(player.getName().getString(), player.getUUID(), component));

            return EventResult.pass();
        });

        connector.connect();
        connector.publish(MessageHelper.buildServerOnlineMessage());
    }

    public void stop(MinecraftServer server) {
        connector.publish(MessageHelper.buildServerOfflineMessage());
        connector.disconnect();
    }
}
