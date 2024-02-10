package cx.rain.mc.catmessenger.bukkit.networking.packet.s2c;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.networking.payload.MessagePayload;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.exception.MalformedPacketException;
import cx.rain.mc.catmessenger.common.CatMessenger;

public class ForwardS2CPacket extends S2CPacket {
    @Override
    public void handle(CatMessengerBukkit plugin, CborReader reader) {
        var publisher = reader.readString();
        var channel = reader.readString();
        var bytes = reader.readBytes();

        if (!channel.equals(CatMessenger.CHANNEL_ID)) {
            return;
        }

        try {
            MessagePayload.handle(publisher, bytes);
        } catch (MalformedPacketException ex) {
            plugin.getLogger().warning(ex.toString());
        }
    }
}
