package cx.rain.mc.catmessenger.bungee.socket;

import cx.rain.mc.catmessenger.bungee.MessengerBungee;
import cx.rain.mc.catmessenger.bungee.config.ConfigManager;
import cx.rain.mc.catmessenger.bungee.utility.MessageSendHelper;
import cx.rain.mc.catmessenger.common.Constants;
import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoServer;

public class SocketIOServerManager {
    private final ConfigManager configManager;

    private final EngineIoServer engineIoServer;
    private final SocketIoServer server;

    public SocketIOServerManager(MessengerBungee plugin) {
        configManager = plugin.getConfigManager();

        engineIoServer = new EngineIoServer();
        server = new SocketIoServer(engineIoServer);

        var namespace = server.namespace("/");
        namespace.on(Constants.EVENT_MESSAGE, args -> {
            if (args.length != 5) {
                return;
            }

            var platform = args[0].toString();
            var from = args[1].toString();
            var sender = args[2].toString();
            var time = args[3].toString();
            var content = args[4].toString();

            if (platform.equalsIgnoreCase(Constants.CHANNEL_PLATFORM_MINECRAFT_BUKKIT)) {
                MessageSendHelper.broadcastMessage(platform, from, sender, time, content);
            }
        });
    }

    public void start() {
    }
}
