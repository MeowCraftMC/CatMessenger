package cx.rain.mc.catmessenger.bukkit.networking;

import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.networking.packet.c2s.AuthenticateC2SPacket;
import cx.rain.mc.catmessenger.bukkit.networking.packet.c2s.C2SPacket;
import cx.rain.mc.catmessenger.bukkit.networking.packet.c2s.RegisterC2SPacket;
import cx.rain.mc.catmessenger.bukkit.utility.exception.MalformedPacketException;
import cx.rain.mc.catmessenger.bukkit.utility.exception.NotSupportedPacketException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class ConnectorClient extends WebSocketClient {
    private MessengerBukkit plugin;
    private ServerPacketHandler handler;

    public ConnectorClient(MessengerBukkit plugin) throws URISyntaxException {
        super(new URI(plugin.getConfigManager().getRemoteConnector()));

        this.plugin = plugin;
        handler = new ServerPacketHandler(plugin);

        setConnectionLostTimeout(0);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        plugin.getLogger().info("Connected!");

        send(new AuthenticateC2SPacket(plugin.getConfigManager().getServerName(), plugin.getConfigManager().getConnectorSecret()));
        send(new RegisterC2SPacket());
    }

    @Override
    public void onMessage(String message) {
        // Do nothing.
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            handler.handleBytes(bytes.array());
        } catch (NotSupportedPacketException ex) {
            plugin.getLogger().warning("Not implemented packet! " + ex);
        } catch (MalformedPacketException ex) {
            plugin.getLogger().warning("Malformed packet! " + ex);
        }
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

    public void send(C2SPacket packet) {
        try {
            send(packet.toBytes());
        } catch (Exception ex) {
            plugin.getLogger().severe("Cannot send message! " + ex);
        }
    }
}
