package cx.rain.mc.catmessenger.bukkit.utility;

import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import cx.rain.mc.catmessenger.common.Constants;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageSender {
    public static void sendSystemMessage(String content) {
        sendMessage("", content);
    }

    public static void sendChatMessage(Player player, String content) {
        var sender = player.getDisplayName();   // Todo: qyl27: Vault chat api support.
        sendMessage(sender, content);
    }

    private static void sendMessage(String sender, String content) {
        var platform = Constants.CHANNEL_PLATFORM_MINECRAFT_BUKKIT;
        var from = MessengerBukkit.getInstance().getConfigManager().getServerName();
        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));

        MessengerBukkit.getInstance().getSocketIoClientManager().sendMessage(platform, from, sender, time, content);
//        server.sendPluginMessage(MessengerBukkit.getInstance(), CatMessenger.MESSAGES_CHANNEL_NAME, output.toByteArray());
    }
}
