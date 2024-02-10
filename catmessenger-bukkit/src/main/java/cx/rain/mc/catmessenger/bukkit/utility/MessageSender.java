package cx.rain.mc.catmessenger.bukkit.utility;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.networking.packet.c2s.C2SPacket;

public class MessageSender {
//    public static void sendSystemMessage(Server server, String content) {
//        sendMessage(server, "", content);
//    }
//
//    public static void sendChatMessage(Player player, String content) {
//        var sender = player.getDisplayName();   // Todo: qyl27: Vault chat api support.
//        sendMessage(player.getServer(), sender, content);
//    }
//
//    private static void sendMessage(Server server, String sender, String content) {
//        var platform = Constants.CHANNEL_PLATFORM_MINECRAFT_BUKKIT;
//        var from = MessengerBukkit.getInstance().getConfigManager().getServerName();
//        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
//
//        var output = ByteStreams.newDataOutput();
//        output.writeUTF(platform);
//        output.writeUTF(from);
//        output.writeUTF(sender);
//        output.writeUTF(time);
//        output.writeUTF(content);
//
//        server.sendPluginMessage(MessengerBukkit.getInstance(), Constants.MESSAGES_CHANNEL_NAME, output.toByteArray());
//    }

    private static void sendMessageInternal(C2SPacket packet) {
        CatMessengerBukkit.getInstance().getConnectorClient().send(packet.toBytes());
    }
}
