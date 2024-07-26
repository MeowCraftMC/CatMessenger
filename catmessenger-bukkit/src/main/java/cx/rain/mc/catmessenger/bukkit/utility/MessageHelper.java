package cx.rain.mc.catmessenger.bukkit.utility;

import cx.rain.mc.catmessenger.connector.ConnectorMessage;
import cx.rain.mc.catmessenger.message.AbstractMessage;
import cx.rain.mc.catmessenger.message.MessageColor;
import cx.rain.mc.catmessenger.message.type.EmptyMessage;
import cx.rain.mc.catmessenger.message.type.NewlineMessage;
import cx.rain.mc.catmessenger.message.type.TextMessage;
import cx.rain.mc.catmessenger.message.type.TranslatableMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.advancement.AdvancementDisplayType;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MessageHelper {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<BaseComponent> toBroadcast(ConnectorMessage message) {
        var result = new TextComponent("[" + message.getClient() + "] ");
        result.setColor(ChatColor.GREEN);
        result.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(message.getTime().toLocalDateTime().format(FORMATTER))));

        var result2 = new TextComponent();
        if (message.getSender() != null) {
            result2 = new TextComponent("<");
            result2.setColor(ChatColor.YELLOW);
            var result22 = toComponent(message.getSender());
            result22.setColor(ChatColor.YELLOW);
            result2.addExtra(result22);
            var result23 = new TextComponent("> ");
            result23.setColor(ChatColor.YELLOW);
            result2.addExtra(result23);
        }

        var result3 = toComponent(message.getContent());
        return List.of(result, result2, result3);
    }

    private static BaseComponent toComponent(AbstractMessage message) {
        BaseComponent component = createComponent(message);

        if (component == null) {
            return new TextComponent();
        }

        component.setBold(message.isBold());
        component.setItalic(message.isItalic());
        component.setUnderlined(message.isUnderline());
        component.setStrikethrough(message.isStrikethrough());
        component.setObfuscated(message.isSpoiler());

        var color = ChatColor.of(message.getColor().asString());
        if (color != ChatColor.RESET) {
            component.setColor(color);
        }

        if (message.hasHoverMessage()) {
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    toMultiLineComponent(toComponent(message.getHoverMessage()))
                            .stream()
                            .map(Text::new)
                            .toArray(Text[]::new)));
        }

        if (message.hasClickEvent()) {
            var action = switch (message.getClickEvent()) {
                case OPEN_URL -> ClickEvent.Action.OPEN_URL;
                case RUN_COMMAND -> ClickEvent.Action.RUN_COMMAND;
                case SUGGEST_COMMAND -> ClickEvent.Action.SUGGEST_COMMAND;
                case COPY -> ClickEvent.Action.COPY_TO_CLIPBOARD;
            };
            component.setClickEvent(new ClickEvent(action, message.getClickValue()));
        }

        if (message.hasExtra()) {
            for (var extra : message.getExtras()) {
                component.addExtra(toComponent(extra));
            }
        }

        return component;
    }

    private static List<BaseComponent> toMultiLineComponent(BaseComponent message) {
        var list = new ArrayList<BaseComponent>();

        var component = message;

        if (component.getExtra() == null || component.getExtra().isEmpty()) {
            list.add(component);
            return list;
        }

        var extras = List.copyOf(component.getExtra());
        component.getExtra().clear();
        for (var extra : extras) {
            if (extra != null) {
                component.addExtra(extra);
            } else {
                list.add(component);
                component = new TextComponent();
            }
        }
        list.add(component);

        return list;
    }

    private static BaseComponent createComponent(AbstractMessage message) {
        BaseComponent component = null;

        if (message instanceof EmptyMessage) {
            component = new TextComponent();
        } else if (message instanceof TextMessage text) {
            component = new TextComponent(text.getText());
        } else if (message instanceof TranslatableMessage translatable) {
            component = new TranslatableComponent(translatable.getKey(), translatable.getArgs());
        } else if (message instanceof NewlineMessage newline) {
            // Do nothing.
        }

        return component;
    }

    public static AbstractMessage buildServerOnlineMessage() {
        var message = new TextMessage("服务器上线了");
        message.setColor(MessageColor.YELLOW);
        return message;
    }

    public static AbstractMessage buildServerOfflineMessage() {
        var message = new TextMessage("服务器离线了");
        message.setColor(MessageColor.YELLOW);
        return message;
    }

    public static AbstractMessage buildJoinMessage(String playerName) {
        var message = new TextMessage(playerName);
        message.setColor(MessageColor.YELLOW);
        var message2 = new TextMessage(" 登入了服务器");
        message2.setColor(MessageColor.YELLOW);
        message.getExtras().add(message2);
        return message;
    }

    public static AbstractMessage buildQuitMessage(String playerName) {
        var message = new TextMessage(playerName);
        message.setColor(MessageColor.YELLOW);
        var message2 = new TextMessage(" 登出了服务器");
        message2.setColor(MessageColor.YELLOW);
        message.getExtras().add(message2);
        return message;
    }

    public static ConnectorMessage buildChatMessage(String playerName, UUID uuid, BaseComponent content) {
        var connectorMessage = new ConnectorMessage();
        var sender = new TextMessage(playerName);
        sender.setHoverMessage(new TextMessage(uuid.toString()));
        connectorMessage.setSender(sender);

        var message = new EmptyMessage();

        var list = new ArrayList<BaseComponent>();
        list.add(content);
        while (!list.isEmpty()) {
            var component = list.get(0);
            if (component.getExtra() != null && !component.getExtra().isEmpty()) {
                list.addAll(component.getExtra());
            }

            var text = new TextMessage(component.toPlainText());
            text.setColor(MessageColor.fromString(component.getColor().toString()));
            text.setBold(component.isBold());
            text.setItalic(component.isItalic());
            text.setStrikethrough(component.isStrikethrough());
            text.setUnderline(component.isUnderlined());
            text.setSpoiler(component.isObfuscated());

            message.getExtras().add(text);
            list.remove(0);
        }

        connectorMessage.setContent(message);
        return connectorMessage;
    }

    public static AbstractMessage buildAdvancementMessage(String playerName, String title, String description, AdvancementDisplayType type) {
        var message = new TextMessage(playerName);
        message.setColor(MessageColor.WHITE);

        var message2 = switch (type) {
            case TASK -> new TextMessage(" 取得了进度 ");
            case GOAL -> new TextMessage(" 完成了目标 ");
            case CHALLENGE -> new TextMessage(" 完成了挑战 ");
        };
        message2.setColor(MessageColor.WHITE);

        var message3 = new TextMessage("[" + title + "] ");
        switch (type) {
            case TASK -> message3.setColor(MessageColor.GREEN);
            case GOAL -> message3.setColor(MessageColor.AQUA);
            case CHALLENGE -> message3.setColor(MessageColor.DARK_PURPLE);
        }

        message3.setHoverMessage(new TextMessage(description));

        var extras = message.getExtras();
        extras.add(message2);
        extras.add(message3);
        return message;
    }

    public static AbstractMessage buildPlayerDeathMessage(String playerName, String deathMessage) {
        var message = new TextMessage(deathMessage);
        message.setColor(MessageColor.WHITE);
        return message;
    }
}
