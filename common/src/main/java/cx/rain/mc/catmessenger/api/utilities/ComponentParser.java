package cx.rain.mc.catmessenger.api.utilities;

import cx.rain.mc.catmessenger.api.model.Message;
import cx.rain.mc.catmessenger.api.model.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;

import java.time.format.DateTimeFormatter;

import static cx.rain.mc.catmessenger.api.utilities.ComponentSerializer.fromJson;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

public class ComponentParser {
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Component parseFrom(Message message) {
        var result = empty()
                .append(parsePlatform(message.getPlatform())
                        .hoverEvent(text(message.getTime().toLocalDateTime().format(FORMATTER))));

        if (message.getSender() != null) {
            result = result.append(parseSender(message.getSender()));
        }

        return result.append(fromJson(message.getContent()));
    }

    private static Component parsePlatform(String platform) {
        return text("[" + fromJson(platform) + "] ")
                .color(NamedTextColor.GREEN);
    }

    private static Component parseSender(Player player) {
        return text("<")
                .color(NamedTextColor.YELLOW)
                .append(parsePlayer(player))
                .append(text("> "));
    }

    public static Component parsePlayer(Player player) {
        var result = empty();

        if (player.getPrefix() != null) {
            result = result.append(text("[" + fromJson(player.getPrefix()) + "]").style(Style.empty()));
        }

        Component name;
        Component hover = null;
        if (player.getName() != null) {
            name = fromJson(player.getName());
            hover = text(player.getId());
        } else {
            name = text(player.getId());
        }

        if (player.getUuid() != null) {
            if (hover != null) {
                hover = hover.append(text("(" + player.getUuid() + ")"));
            } else {
                hover = text(player.getUuid().toString());
            }
        }

        result = result.append(name.hoverEvent(hover));

        if (player.getSuffix() != null) {
            result = result.append(text("[" + fromJson(player.getSuffix()) + "]").style(Style.empty()));
        }

        return result;
    }
}
