package cx.rain.mc.catmessenger.paper.utility;

import cx.rain.mc.catmessenger.api.model.Player;
import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
import cx.rain.mc.catmessenger.api.utilities.ComponentParser;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BukkitMessageHelper {

    public static Player createPlayer(org.bukkit.entity.Player player) {
        return new Player(player.getName(), player.getUniqueId(), ComponentSerializer.toJson(player.displayName()));
    }

    public static Player createPlayer(@Nullable String name, @Nullable UUID uuid) {
        if (name == null) {
            return createPlayer("无名氏", uuid);
        }
        return new Player(name, uuid);
    }

    public static Component createLocation(Location location) {
        var result = String.format("(%d, %d, %d)", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var world = location.getWorld();
        if (world != null) {
            result = world.getName() + result;
        }
        return Component.text(result);
    }

    public static Component createEntityName(Entity entity) {
        return Component.translatable(entity.getType().translationKey());
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
            case CHALLENGE -> NamedTextColor.DARK_PURPLE;
            case GOAL -> NamedTextColor.AQUA;
            case TASK -> NamedTextColor.GREEN;
        };

        return result.append(Component.text("[" + ComponentSerializer.toPlain(title) + "]")
                        .color(color))
                .hoverEvent(Component.text(ComponentSerializer.toPlain(description)));
    }

    public static Component petDeath(Player player, Component message) {
        return message.append(Component.text("，"))
                .append(ComponentParser.parsePlayer(player).append(Component.text(" 非常伤心")));
    }

    public static Component namedDeath(Component name, Component entityTypeName, Location location) {
        return entityTypeName.append(Component.text(" "))
                .append(name)
                .append(Component.text(" 长眠于 "))
                .append(createLocation(location));
    }
}
