package cx.rain.mc.catmessenger.api.utilities;

import cx.rain.mc.catmessenger.api.message.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MessageFactory {

    public static Component serverOnline() {
        return serverOnline(false);
    }

    public static Component serverOnline(boolean bold) {
        return Component.text("服务器上线了").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, bold);
    }

    public static Component serverOffline() {
        return serverOffline(false);
    }

    public static Component serverOffline(boolean bold) {
        return Component.text("服务器离线了").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, bold);
    }

    public static Component playerJoined(Player player) {
        return Component.empty()
                .append(MessageParser.parsePlayer(player))
                .append(Component.text("登入了服务器"))
                .color(NamedTextColor.YELLOW);
    }

    public static Component playerLeft(Player player) {
        return Component.empty()
                .append(MessageParser.parsePlayer(player))
                .append(Component.text("登出了服务器"))
                .color(NamedTextColor.YELLOW);
    }
}
