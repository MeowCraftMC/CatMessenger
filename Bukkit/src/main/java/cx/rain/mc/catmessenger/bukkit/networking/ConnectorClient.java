package cx.rain.mc.catmessenger.bukkit.networking;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.networking.packet.c2s.AuthenticateC2SPacket;
import cx.rain.mc.catmessenger.bukkit.networking.packet.c2s.C2SPacket;
import cx.rain.mc.catmessenger.bukkit.networking.packet.c2s.PublishC2SPacket;
import cx.rain.mc.catmessenger.bukkit.networking.packet.c2s.RegisterC2SPacket;
import cx.rain.mc.catmessenger.bukkit.networking.payload.MessagePayload;
import cx.rain.mc.catmessenger.bukkit.networking.payload.ServerLifecyclePayload;
import cx.rain.mc.catmessenger.bukkit.utility.exception.MalformedPacketException;
import cx.rain.mc.catmessenger.bukkit.utility.exception.NotSupportedPacketException;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class ConnectorClient extends WebSocketClient {
    private final CatMessengerBukkit plugin;
    private final ServerPacketHandler handler;

    public ConnectorClient(CatMessengerBukkit plugin) throws URISyntaxException {
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

        send(new ServerLifecyclePayload(true));
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::reconnect, 1);
        }

        plugin.getLogger().info("Disconnected! " + reason);
    }

    @Override
    public void onError(Exception ex) {
        plugin.getLogger().warning(ex.toString());
        // Todo: qyl27: Need reconnect?
    }

    public void send(C2SPacket packet) {
        try {
            send(packet.toBytes());
        } catch (Exception ex) {
            plugin.getLogger().severe("Cannot send packet! " + ex);
        }
    }

    public void send(MessagePayload payload) {
        try {
            send(new PublishC2SPacket(payload));
        } catch (Exception ex) {
            plugin.getLogger().severe("Cannot send payload! " + ex);
        }
    }
}
