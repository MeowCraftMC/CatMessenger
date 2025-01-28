package cx.rain.mc.catmessenger.paper.utility;

import cx.rain.mc.catmessenger.api.message.Message;
import cx.rain.mc.catmessenger.api.message.Player;
import cx.rain.mc.catmessenger.api.utilities.ComponentParser;
import cx.rain.mc.catmessenger.paper.CatMessengerPaper;
import net.kyori.adventure.text.Component;

public class MessengerHelper {
    public static void send(Component content) {
        sendInternal(null, content);
    }

    public static void send(org.bukkit.entity.Player player, Component content) {
        sendInternal(BukkitMessageHelper.createPlayer(player), content);
    }

    private static void sendInternal(Player player, Component content) {
        var plugin = CatMessengerPaper.getInstance();
        var platform = plugin.getConfigManager().getName();
        var message = new Message(platform, player, ComponentParser.toMiniMessage(content));
        plugin.getMessenger().sendMessage(message);
    }
}
