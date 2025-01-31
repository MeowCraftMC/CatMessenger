package cx.rain.mc.catmessenger.bungee.utility;

import cx.rain.mc.catmessenger.message.AbstractMessage;
import cx.rain.mc.catmessenger.message.MessageColor;
import cx.rain.mc.catmessenger.message.type.TextMessage;

public class MessageHelper {
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
}
