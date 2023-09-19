package cx.rain.mc.catmessenger.bukkit.networking;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.networking.packet.s2c.ForwardS2CPacket;
import cx.rain.mc.catmessenger.bukkit.networking.packet.s2c.S2CPacket;
import cx.rain.mc.catmessenger.bukkit.networking.packet.s2c.SuccessfulS2CPacket;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.exception.NotSupportedPacketException;

import java.util.HashMap;
import java.util.Map;

public class ServerPacketHandler {
    private static final Map<String, S2CPacket> HANDLERS = new HashMap<>();

    static {
        HANDLERS.put("Successful", new SuccessfulS2CPacket());
        HANDLERS.put("Forward", new ForwardS2CPacket());
    }

    private final CatMessengerBukkit plugin;

    public ServerPacketHandler(CatMessengerBukkit plugin) {
        this.plugin = plugin;
    }

    public void handleBytes(byte[] bytes) {
        var reader = new CborReader(bytes);
        reader.readStartArray();
        var operation = reader.readString();

        var handler = HANDLERS.get(operation);
        if (handler == null) {
            throw new NotSupportedPacketException();
        }

        handler.handle(plugin, reader);

        reader.readEndArray();
    }
}
