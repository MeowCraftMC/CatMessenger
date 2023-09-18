package cx.rain.mc.catmessenger.bukkit.networking.packet.s2c;

import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.networking.payload.MessagePayload;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.exception.MalformedPacketException;
import cx.rain.mc.catmessenger.common.Constants;

public class ForwardS2CPacket extends S2CPacket {
    @Override
    public void handle(MessengerBukkit plugin, CborReader reader) {
        var publisher = reader.readString();
        var channel = reader.readString();
        var bytes = reader.readBytes();

        if (!channel.equals(Constants.CHANNEL_ID)) {
            return;
        }

        try {
            MessagePayload.handle(publisher, bytes);
        } catch (MalformedPacketException ex) {
            plugin.getLogger().warning(ex.toString());
        }
    }
}
