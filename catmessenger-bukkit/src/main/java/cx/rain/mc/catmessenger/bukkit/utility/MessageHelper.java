package cx.rain.mc.catmessenger.bukkit.utility;

import cx.rain.mc.catmessenger.message.AbstractMessage;
import cx.rain.mc.catmessenger.message.IMessage;
import cx.rain.mc.catmessenger.message.MessageColor;
import cx.rain.mc.catmessenger.message.type.EmptyMessage;
import cx.rain.mc.catmessenger.message.type.TextMessage;
import cx.rain.mc.catmessenger.message.type.TranslatableMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.advancement.AdvancementDisplayType;

public class MessageHelper {
    public static BaseComponent toBroadcast(String sender, IMessage message) {
        var result = new TextComponent("[" + sender + "] ");
        result.setColor(ChatColor.YELLOW);
        result.addExtra(toComponent(message));
        return result;
    }

    private static BaseComponent toComponent(IMessage message) {
        BaseComponent component = null;

        if (message instanceof EmptyMessage) {
            component = new TextComponent();
        } else if (message instanceof TextMessage text) {
            component = new TextComponent(text.getText());
        } else if (message instanceof TranslatableMessage translatable) {
            component = new TranslatableComponent(translatable.getKey(), translatable.getArgs());
        }

        if (component == null) {
            throw new RuntimeException("Not supported message: " + message);
        }

        if (message instanceof AbstractMessage abstractMessage) {
            component.setBold(abstractMessage.isBold());
            component.setItalic(abstractMessage.isItalic());
            component.setUnderlined(abstractMessage.isUnderline());
            component.setStrikethrough(abstractMessage.isStrikethrough());
            component.setObfuscated(abstractMessage.isSpoiler());

            component.setColor(ChatColor.of(abstractMessage.getColor().asString()));
        }

        if (message.hasExtra()) {
            for (var extra : message.getExtras()) {
                component.addExtra(toComponent(extra));
            }
        }

        return component;
    }

    public static IMessage buildServerOnlineMessage() {
        var message = new TextMessage("服务器上线了");
        message.setBold(true);
        message.setColor(MessageColor.YELLOW);
        return message;
    }

    public static IMessage buildServerOfflineMessage() {
        var message = new TextMessage("服务器离线了");
        message.setBold(true);
        message.setColor(MessageColor.YELLOW);
        return message;
    }

    public static IMessage buildJoinMessage(String playerName) {
        var message = new TextMessage(playerName);
        message.setColor(MessageColor.YELLOW);
        var message2 = new TextMessage(" 加入了服务器");
        message2.setColor(MessageColor.YELLOW);
        message.getExtras().add(message2);
        return message;
    }

    public static IMessage buildQuitMessage(String playerName) {
        var message = new TextMessage(playerName);
        message.setColor(MessageColor.YELLOW);
        var message2 = new TextMessage(" 登出了服务器");
        message2.setColor(MessageColor.YELLOW);
        message.getExtras().add(message2);
        return message;
    }

    public static IMessage buildChatMessage(String playerName, String content) {
        var message = new TextMessage("<" + playerName + "> ");
        message.setColor(MessageColor.WHITE);
        var message2 = new TextMessage(content);
        message.getExtras().add(message2);
        return message;
    }

    public static IMessage buildAdvancementMessage(String playerName, String title, String description, AdvancementDisplayType type) {
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

        // Todo: Hover-able message3 to show description.

        var extras = message.getExtras();
        extras.add(message2);
        extras.add(message3);
        return message;
    }
}
