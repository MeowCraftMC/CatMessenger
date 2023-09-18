package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import org.bukkit.Bukkit;

public class RawPayload extends MessagePayload {
    private final String text;

    public RawPayload(String text) {
        this.text = text;
    }

    public RawPayload(CborReader reader) {
        text = reader.readString();
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeString(text);
    }

    @Override
    protected void handle(String publisher) {
        Bukkit.getServer().broadcastMessage("[" + publisher + "] " + text);
    }
}
