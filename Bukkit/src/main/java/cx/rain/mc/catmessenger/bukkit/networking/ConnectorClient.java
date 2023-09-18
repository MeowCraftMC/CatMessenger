package cx.rain.mc.catmessenger.bukkit.networking;

import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class ConnectorClient extends WebSocketClient {
    private MessengerBukkit plugin;

    public ConnectorClient(MessengerBukkit plugin) throws URISyntaxException {
        super(new URI(plugin.getConfigManager().getRemoteConnector()));

        this.plugin = plugin;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {


        plugin.getLogger().info("Connected!");
    }

    @Override
    public void onMessage(String message) {
        // Do nothing.
    }

    @Override
    public void onMessage(ByteBuffer bytes) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        plugin.getLogger().info("Disconnected!");
    }

    @Override
    public void onError(Exception ex) {
        plugin.getLogger().warning(ex.toString());
    }
}
