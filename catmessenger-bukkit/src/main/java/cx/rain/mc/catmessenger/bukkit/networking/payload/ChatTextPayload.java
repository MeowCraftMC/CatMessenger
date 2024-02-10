package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import org.bukkit.Bukkit;

public class ChatTextPayload extends MessagePayload {
    private final String sender;
    private final String content;

    public ChatTextPayload(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public ChatTextPayload(CborReader reader) {
        sender = reader.readString();
        content = reader.readString();
    }

    @Override
    protected MessagePayloadType getType() {
        return MessagePayloadType.CHAT_TEXT;
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeString(sender);
        writer.writeString(content);
    }

    @Override
    protected void handle(String publisher) {
        var formatted = String.format(CatMessengerBukkit.getInstance().getConfigManager().getFormatChat(),
                publisher, sender, content);
        CatMessengerBukkit.getInstance().getLogger().info(formatted);
        Bukkit.getServer().broadcastMessage(formatted);
    }
}
