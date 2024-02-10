package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import org.bukkit.Bukkit;

public class PlayerOnlinePayload extends MessagePayload {
    private final boolean online;
    private final String playerName;

    public PlayerOnlinePayload(boolean online, String name) {
        this.online = online;
        this.playerName = name;
    }

    public PlayerOnlinePayload(CborReader reader) {
        online = reader.readBoolean();
        playerName = reader.readString();
    }

    @Override
    protected MessagePayloadType getType() {
        return MessagePayloadType.PLAYER_ONLINE;
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeBoolean(online);
        writer.writeString(playerName);
    }

    @Override
    protected void handle(String publisher) {
        var formatted = String.format(online ?
                        CatMessengerBukkit.getInstance().getConfigManager().getFormatPlayerOnline()
                        : CatMessengerBukkit.getInstance().getConfigManager().getFormatPlayerOffline(),
                publisher, playerName);
        CatMessengerBukkit.getInstance().getLogger().info(formatted);
        Bukkit.getServer().broadcastMessage(formatted);
    }
}
