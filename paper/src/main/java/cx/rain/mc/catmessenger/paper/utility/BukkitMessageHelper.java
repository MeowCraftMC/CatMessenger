package cx.rain.mc.catmessenger.paper.utility;

import cx.rain.mc.catmessenger.api.model.Player;
import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
import cx.rain.mc.catmessenger.api.utilities.ComponentParser;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BukkitMessageHelper {

    public static Player createPlayer(org.bukkit.entity.Player player) {
        return new Player(player.getName(), player.getUniqueId(), ComponentSerializer.toLegacy(player.displayName()));
    }

    public static Component playerAdvancement(Player player, Component title, Component description, AdvancementDisplay.Frame frame) {
        var result = ComponentParser.parsePlayer(player);

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
