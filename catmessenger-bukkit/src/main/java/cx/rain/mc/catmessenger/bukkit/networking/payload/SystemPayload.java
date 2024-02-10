package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import org.bukkit.Bukkit;

public class SystemPayload extends MessagePayload {
    private final String message;

    public SystemPayload(String content) {
        this.message = content;
    }

    public SystemPayload(CborReader reader) {
        message = reader.readString();
    }

    @Override
    protected MessagePayloadType getType() {
        return MessagePayloadType.SYSTEM;
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeString(message);
    }

    @Override
    protected void handle(String publisher) {
        var formatted = String.format(CatMessengerBukkit.getInstance().getConfigManager().getFormatSystem(),
                publisher, message);
        CatMessengerBukkit.getInstance().getLogger().info(formatted);
        Bukkit.getServer().broadcastMessage(formatted);
    }
}
