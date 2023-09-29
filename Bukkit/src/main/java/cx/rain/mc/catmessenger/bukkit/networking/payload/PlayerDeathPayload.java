package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import org.bukkit.Bukkit;

public class PlayerDeathPayload extends MessagePayload {
    private String pattern;
    private String playerName;
    private String killerName;
    private String itemName;

    public PlayerDeathPayload(String pattern, String playerName, String killerName, String itemName) {
        this.pattern = pattern;
        this.playerName = playerName;
        this.killerName = killerName;
        this.itemName = itemName;
    }

    public PlayerDeathPayload(CborReader reader) {
        pattern = reader.readString();
        playerName = reader.readString();
        killerName = reader.readString();
        itemName = reader.readString();
    }

    @Override
    protected MessagePayloadType getType() {
        return MessagePayloadType.PLAYER_DEATH;
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeString(pattern);
        writer.writeString(playerName);
        writer.writeString(killerName);
        writer.writeString(itemName);
    }

    @Override
    protected void handle(String publisher) {
        var patterned = String.format(pattern, playerName, killerName, itemName);
        var formatted = String.format(CatMessengerBukkit.getInstance().getConfigManager().getFormatPlayerDeath(),
                publisher, patterned);
        CatMessengerBukkit.getInstance().getLogger().info(formatted);
        Bukkit.getServer().broadcastMessage(formatted);
    }
}
