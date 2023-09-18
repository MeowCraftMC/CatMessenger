package cx.rain.mc.catmessenger.bungee.utility;

import com.google.common.io.ByteStreams;
import cx.rain.mc.catmessenger.bungee.MessengerBungee;
import cx.rain.mc.catmessenger.common.Constants;
import net.md_5.bungee.api.ProxyServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageSendHelper {
    public static void broadcastMessage(String platform, String from, String sender, String time, String content) {
        var servers = ProxyServer.getInstance().getServersCopy();

        var output = ByteStreams.newDataOutput();
        output.writeUTF(platform);
        output.writeUTF(from);
        output.writeUTF(sender);
        output.writeUTF(time);
        output.writeUTF(content);
        var data = output.toByteArray();

        for (var entry : servers.entrySet()) {
            entry.getValue().sendData(Constants.CHANNEL_ID, data);
        }

        if (!platform.equalsIgnoreCase(Constants.CHANNEL_PLATFORM_TELEGRAM)) {
            MessengerBungee.getInstance().getBot().sendMessage(sender, content);
        }
    }

    public static void sendSystemMessage(String content) {
        var platform = Constants.CHANNEL_PLATFORM_MINECRAFT_BUNGEE;
        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));

        broadcastMessage(platform, "BungeeCord", "", time, content);
    }
}
