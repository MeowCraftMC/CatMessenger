package cx.rain.mc.catmessenger.bukkit;

import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.handler.AsyncPlayerChatHandler;
import cx.rain.mc.catmessenger.bukkit.handler.PlayerEventHandler;
import cx.rain.mc.catmessenger.bukkit.utility.MessageHelper;
import cx.rain.mc.catmessenger.connector.ConnectorCommand;
import cx.rain.mc.catmessenger.connector.RabbitMQConnector;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class CatMessengerBukkit extends JavaPlugin {
    private static CatMessengerBukkit INSTANCE;

    private final Logger logger = getLogger();
    private final ConfigManager config = new ConfigManager(this);
    @Getter
    private final RabbitMQConnector connector;

    public CatMessengerBukkit() {
        INSTANCE = this;

        connector = new RabbitMQConnector(config.getId(), config.getConnectorRetry(),
                config.getConnectorHost(), config.getConnectorPort(), config.getConnectorVirtualHost(),
                config.getConnectorUsername(), config.getConnectorPassword());

        connector.getMessageQueue().addHandler(message -> Bukkit.getScheduler()
                .scheduleSyncDelayedTask(this, () -> {
                    var components = MessageHelper.toBroadcast(message).toArray(BaseComponent[]::new);
                    Bukkit.spigot().broadcast(components);
                    var component = new ComponentBuilder().append(components).build();
                    logger.info(component.toPlainText());
                }));

        connector.getCommandQueue().addHandler((command, props) -> {
            if (!command.getCallback().equals(config.getName())) {
                return;
            }

            switch (command.getCommand()) {
                case QUERY_ONLINE -> {
                    var response = new ConnectorCommand();
                    response.setSender(config.getName());
                    response.setCallback(command.getSender());
                    response.setReplyTo(command.getReplyTo());
                    response.setCommand(ConnectorCommand.EnumCommand.RESPONSE_ONLINE);
                    var players = Bukkit.getServer().getOnlinePlayers();
                    var args = new ArrayList<String>();
                    args.add(Integer.toString(players.size()));
                    args.addAll(players.stream().map(Player::getDisplayName).toList());
                    response.setArguments(args.toArray(String[]::new));
//                    connector.getCommandQueue().publish(response);
                }
                case QUERY_WORLD_TIME -> {
                    var response = new ConnectorCommand();
                    response.setSender(config.getName());
                    response.setCallback(command.getSender());
                    response.setReplyTo(command.getReplyTo());
                    response.setCommand(ConnectorCommand.EnumCommand.RESPONSE_ONLINE);

                    var worldName = command.getArguments()[0];
                    var typeName = command.getArguments()[1];

                    var world = Bukkit.getServer().getWorld(worldName);
                    if (world == null) {
                        response.setArguments(new String[]{"-1"});
//                        connector.getCommandQueue().publish(response);
                        return;
                    }

                    var time = -1L;
                    if (typeName.equalsIgnoreCase("daytime")) {
                        time = world.getTime();
                    } else if (typeName.equalsIgnoreCase("gametime")) {
                        time = world.getGameTime();
                    } else if (typeName.equalsIgnoreCase("day")) {
                        time = world.getFullTime() / 24000;
                    }
                    response.setArguments(new String[]{Long.toString(time)});
//                    connector.getCommandQueue().publish(response);
                }
                case ERROR, ONLINE, OFFLINE, RESPONSE_ONLINE, RESPONSE_WORLD_TIME, RUN_COMMAND, COMMAND_RESULT -> {
                }
            }
        });
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading CatMessenger.");

        getConnector().connect();

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);

        getConnector().publish(MessageHelper.buildServerOnlineMessage());

        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        getConnector().publish(MessageHelper.buildServerOfflineMessage());

        getConnector().disconnect();

        getLogger().info("Bye~");
    }

    public static CatMessengerBukkit getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return config;
    }
}
