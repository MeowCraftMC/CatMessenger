package cx.rain.mc.catmessenger.bukkit.utility;

import cx.rain.mc.catmessenger.api.message.Player;
import cx.rain.mc.catmessenger.api.utilities.ComponentParser;
import cx.rain.mc.catmessenger.api.utilities.MessageParser;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BukkitMessageHelper {

    public static Player createPlayer(org.bukkit.entity.Player player) {
        return new Player(player.getName(), player.getUniqueId(), ComponentParser.toLegacy(player.displayName()));
    }

    public static Component playerAdvancement(Player player, Component title, Component description, AdvancementDisplay.Frame frame) {
        var result = MessageParser.parsePlayer(player);

        var text = switch (frame) {
            case TASK -> " 取得了进度 ";
            case GOAL -> " 完成了目标 ";
            case CHALLENGE -> " 完成了挑战 ";
        };
        result = result.append(Component.text(text));

        var color = switch (frame) {
            case CHALLENGE -> NamedTextColor.GREEN;
            case GOAL -> NamedTextColor.AQUA;
            case TASK -> NamedTextColor.DARK_PURPLE;
        };

        return result.append(Component.text("[" + title + "]")
                        .color(color))
                .hoverEvent(description);
    }
}
