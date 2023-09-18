package cx.rain.mc.catmessenger.bukkit.networking.packet.c2s;

import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import cx.rain.mc.catmessenger.bukkit.utility.MessageDirection;
import cx.rain.mc.catmessenger.common.Constants;

public class RegisterC2SPacket extends C2SPacket {
    @Override
    protected void write(CborWriter writer) {
        writer.writeString(Constants.PACKET_REGISTER);
        writer.writeString(Constants.CHANNEL_ID);
        writer.writeInt32(MessageDirection.ALL.getId());
    }
}
