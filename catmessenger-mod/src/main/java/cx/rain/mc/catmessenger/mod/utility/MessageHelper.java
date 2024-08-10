package cx.rain.mc.catmessenger.mod.utility;

import cx.rain.mc.catmessenger.connector.ConnectorMessage;
import cx.rain.mc.catmessenger.message.AbstractMessage;
import cx.rain.mc.catmessenger.message.MessageColor;
import cx.rain.mc.catmessenger.message.type.EmptyMessage;
import cx.rain.mc.catmessenger.message.type.NewlineMessage;
import cx.rain.mc.catmessenger.message.type.TextMessage;
import cx.rain.mc.catmessenger.message.type.TranslatableMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.network.chat.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class MessageHelper {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Component toBroadcast(ConnectorMessage message) {
        var result = Component.literal("[" + message.getClient() + "] ");
        result.withStyle(Style.EMPTY
                .withColor(ChatFormatting.GREEN)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        Component.literal(message.getTime().toLocalDateTime().format(FORMATTER)))));

        var result2 = Component.empty();
        if (message.getSender() != null) {
            result2 = Component.literal("<")
                    .withStyle(ChatFormatting.YELLOW)
                    .append(toComponent(message.getSender()).withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal("> ").withStyle(ChatFormatting.YELLOW));
        }

        var result3 = toComponent(message.getContent());
        return result.append(result2).append(result3);
    }

    private static MutableComponent toComponent(AbstractMessage message) {
        var component = createComponent(message);

        if (component == null) {
            return Component.empty();
        }

        var style = Style.EMPTY
                .withBold(message.isBold())
                .withItalic(message.isItalic())
                .withUnderlined(message.isUnderline())
                .withStrikethrough(message.isStrikethrough())
                .withObfuscated(message.isSpoiler());

        var color = TextColor.parseColor(message.getColor().asString());
        color.ifSuccess(style::withColor);

        if (message.hasHoverMessage()) {
            style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, toComponent(message.getHoverMessage())));
        }

        if (message.hasClickEvent()) {
            var action = switch (message.getClickEvent()) {
                case OPEN_URL -> ClickEvent.Action.OPEN_URL;
                case RUN_COMMAND -> ClickEvent.Action.RUN_COMMAND;
                case SUGGEST_COMMAND -> ClickEvent.Action.SUGGEST_COMMAND;
                case COPY -> ClickEvent.Action.COPY_TO_CLIPBOARD;
            };
            style.withClickEvent(new ClickEvent(action, message.getClickValue()));
        }

        if (message.hasExtra()) {
            for (var extra : message.getExtras()) {
                component.append(toComponent(extra));
            }
        }

        return component;
    }

    private static MutableComponent createComponent(AbstractMessage message) {
        MutableComponent component = null;

        if (message instanceof EmptyMessage) {
            component = Component.empty();
        } else if (message instanceof TextMessage text) {
            component = Component.literal(text.getText());
        } else if (message instanceof TranslatableMessage translatable) {
            component = Component.translatable(translatable.getKey(), translatable.getArgs());
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

    public static ConnectorMessage buildChatMessage(String playerName, UUID uuid, Component content) {
        var connectorMessage = new ConnectorMessage();
        var sender = new TextMessage(playerName);
        sender.setHoverMessage(new TextMessage(uuid.toString()));
        connectorMessage.setSender(sender);
        connectorMessage.setContent(toMessage(content));
        return connectorMessage;
    }

    public static AbstractMessage buildAdvancementMessage(String playerName, String title, String description, AdvancementType type) {
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

    public static AbstractMessage buildPlayerDeathMessage(String playerName, Component deathMessage) {
        var message = toMessage(deathMessage);
        message.setColor(MessageColor.WHITE);
        return message;
    }

    public static AbstractMessage toMessage(Component component) {
        var message = new EmptyMessage();

        var list = new ArrayList<Component>();
        list.add(component);
        while (!list.isEmpty()) {
            var c = list.get(0);
            if (!c.getSiblings().isEmpty()) {
                list.addAll(c.getSiblings());
            }

            var text = new TextMessage(c.getString());
            var style = c.getStyle();
            text.setColor(style.getColor() == null ? MessageColor.WHITE : MessageColor.fromString(style.getColor().serialize()));
            text.setBold(style.isBold());
            text.setItalic(style.isItalic());
            text.setStrikethrough(style.isStrikethrough());
            text.setUnderline(style.isUnderlined());
            text.setSpoiler(style.isObfuscated());

            message.getExtras().add(text);
            list.remove(0);
        }

        return message;
    }
}
