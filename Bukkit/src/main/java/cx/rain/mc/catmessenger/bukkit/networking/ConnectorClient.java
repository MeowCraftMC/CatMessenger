package cx.rain.mc.catmessenger.bukkit.networking;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;
import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
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
        var cbor = CBORObject.DecodeFromBytes(bytes.array());
        if (cbor.getType() != CBORType.Array) {
            return;
        }

        // Todo
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (remote && code != CloseFrame.NORMAL) {
            reconnect();
        }

        plugin.getLogger().info("Disconnected! " + reason);
    }

    @Override
    public void onError(Exception ex) {
        plugin.getLogger().warning(ex.toString());
        reconnect();
    }
}
