package cx.rain.mc.catmessenger.bukkit.networking.payload;

import com.google.gson.Gson;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

public class ChatComponentPayload extends MessagePayload {

    private static final Gson GSON = new Gson();

    private final String component;

    public ChatComponentPayload(String component) {
        this.component = component;
    }

    public ChatComponentPayload(CborReader reader) {
        component = reader.readString();
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeString(component);
    }

    @Override
    protected void handle(String publisher) {
        Bukkit.getServer().spigot().broadcast(GSON.fromJson(component, TextComponent.class));
    }
}
