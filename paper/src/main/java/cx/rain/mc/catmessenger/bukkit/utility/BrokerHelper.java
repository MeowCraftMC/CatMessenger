package cx.rain.mc.catmessenger.bukkit.utility;

import cx.rain.mc.catmessenger.api.message.Message;
import cx.rain.mc.catmessenger.api.message.Player;
import cx.rain.mc.catmessenger.api.utilities.ComponentParser;
import cx.rain.mc.catmessenger.api.utilities.MessageParser;
import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class BrokerHelper {
    public static void send(Component content) {
        sendInternal(null, content);
    }

    public static void send(org.bukkit.entity.Player player, Component content) {
        sendInternal(BukkitMessageHelper.createPlayer(player), content);
    }

    private static void sendInternal(Player player, Component content) {
        var plugin = CatMessengerBukkit.getInstance();

        var platform = plugin.getConfigManager().getName();
        var message = new Message(platform, player, ComponentParser.toMiniMessage(content));

        try {
//            Broker.oneway(message);
//            Bukkit.getScheduler()
//                    .scheduleSyncDelayedTask(CatMessengerBukkit.getInstance(), () -> {
//                        var component = MessageParser.parseFrom(message);
//                        Bukkit.broadcast(component);
//                        CatMessengerBukkit.getInstance().getLogger().info(ComponentParser.toPlain(component));
//                    });
        } catch (Throwable ex) {
            plugin.getSLF4JLogger().error("Send failed: ", ex);
        }
    }
}
