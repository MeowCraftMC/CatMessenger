package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import org.bukkit.Bukkit;

public class ServerLifecyclePayload extends MessagePayload {
    private final boolean started;

    public ServerLifecyclePayload(boolean started) {
        this.started = started;
    }

    public ServerLifecyclePayload(CborReader reader) {
        started = reader.readBoolean();
    }

    @Override
    protected MessagePayloadType getType() {
        return MessagePayloadType.SERVER_LIFECYCLE;
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeBoolean(started);
    }

    @Override
    protected void handle(String publisher) {
        var formatted = String.format(started ?
                        CatMessengerBukkit.getInstance().getConfigManager().getFormatServerOnline()
                        : CatMessengerBukkit.getInstance().getConfigManager().getFormatServerOffline(),
                publisher);
        CatMessengerBukkit.getInstance().getLogger().info(formatted);
        Bukkit.getServer().broadcastMessage(formatted);
    }
}
