package cx.rain.mc.catmessenger.bukkit.networking.packet.c2s;

import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;

public abstract class C2SPacket {
    public byte[] toBytes() {
        var writer = new CborWriter();
        writer.writeStartArray();
        write(writer);
        writer.writeEndArray();
        return writer.getBytes();
    }

    protected abstract void write(CborWriter writer);
}
