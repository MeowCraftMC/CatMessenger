package cx.rain.mc.catmessenger.mod.utility;

import cx.rain.mc.catmessenger.api.model.Player;
import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
import cx.rain.mc.catmessenger.api.utilities.ComponentParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.advancements.DisplayInfo;

public class ModMessengerHelper {
    public static Player createPlayer(net.minecraft.world.entity.player.Player player) {
        return new Player(player.getName().getString(), player.getUUID(), ComponentSerializer.toLegacy(toComponent(player.getDisplayName())));
    }

    public static Component toComponent(net.minecraft.network.chat.Component component) {
        return ((ComponentLike) component).asComponent();
    }

    public static Component toComponent(ComponentLike component) {
        return component.asComponent();
    }

    public static Component playerAdvancement(net.minecraft.world.entity.player.Player player, DisplayInfo display) {
        var result = ComponentParser.parsePlayer(createPlayer(player));

        var text = switch (display.getType()) {
            case TASK -> " 取得了进度 ";
            case GOAL -> " 完成了目标 ";
            case CHALLENGE -> " 完成了挑战 ";
        };
        result = result.append(Component.text(text));

        var color = switch (display.getType()) {
            case CHALLENGE -> NamedTextColor.GREEN;
            case GOAL -> NamedTextColor.AQUA;
            case TASK -> NamedTextColor.DARK_PURPLE;
        };

        return result.append(Component.text("[" + display.getTitle() + "]")
                        .color(color))
                .hoverEvent(toComponent(display.getDescription()));
    }
}
