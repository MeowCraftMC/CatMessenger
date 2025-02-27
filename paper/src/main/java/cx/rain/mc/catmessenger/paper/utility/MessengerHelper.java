package cx.rain.mc.catmessenger.paper.utility;

import cx.rain.mc.catmessenger.api.model.Message;
import cx.rain.mc.catmessenger.api.model.Player;
import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
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
        var message = new Message(platform, player, ComponentSerializer.toJson(content));
        plugin.getMessenger().getMessage().publish(message);
    }
}
