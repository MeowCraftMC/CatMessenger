package cx.rain.mc.catmessenger.bukkit.utility;

import com.google.common.io.ByteStreams;
import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import cx.rain.mc.catmessenger.common.CatMessenger;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageSender {
    public static void sendSystemMessage(Server server, String content) {
        sendMessage(server, "", content);
    }

    public static void sendChatMessage(Player player, String content) {
        var sender = player.getDisplayName();   // Todo: qyl27: Vault chat api support.
        sendMessage(player.getServer(), sender, content);
    }

    private static void sendMessage(Server server, String sender, String content) {
        var platform = CatMessenger.CHANNEL_PLATFORM_MINECRAFT_BUKKIT;
        var from = MessengerBukkit.getInstance().getConfigManager().getServerName();
        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));

        var output = ByteStreams.newDataOutput();
        output.writeUTF(platform);
        output.writeUTF(from);
        output.writeUTF(sender);
        output.writeUTF(time);
        output.writeUTF(content);

        server.sendPluginMessage(MessengerBukkit.getInstance(), CatMessenger.MESSAGES_CHANNEL_NAME, output.toByteArray());
    }
}
