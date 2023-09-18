package cx.rain.mc.catmessenger.bukkit.networking.packet.c2s;

import cx.rain.mc.catmessenger.bukkit.networking.payload.MessagePayload;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import cx.rain.mc.catmessenger.common.Constants;

public class PublishC2SPacket extends C2SPacket {
    private final MessagePayload payload;

    public PublishC2SPacket(MessagePayload payload) {
        this.payload = payload;
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeString(Constants.PACKET_PUBLISH);
        writer.writeString(Constants.CHANNEL_ID);
        writer.writeBytes(payload.toBytes());
    }
}
