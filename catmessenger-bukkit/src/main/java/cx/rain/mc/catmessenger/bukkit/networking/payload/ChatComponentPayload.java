package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;

public class ChatComponentPayload extends MessagePayload {
    private final String component;

    public ChatComponentPayload(String component) {
        this.component = component;
    }

    public ChatComponentPayload(CborReader reader) {
        component = reader.readString();
    }

    @Override
    protected MessagePayloadType getType() {
        return MessagePayloadType.CHAT_COMPONENT;
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeString(component);
    }

    @Override
    protected void handle(String publisher) {
        var chatComponent = ComponentSerializer.parse(component);

        var builder = new StringBuilder();
        for (var c : chatComponent) {
            builder.append(c.toPlainText());
        }

        CatMessengerBukkit.getInstance().getLogger().info(builder.toString());
        Bukkit.getServer().spigot().broadcast(chatComponent);
    }
}
