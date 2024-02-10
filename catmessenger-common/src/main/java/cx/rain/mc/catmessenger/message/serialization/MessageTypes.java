package cx.rain.mc.catmessenger.message.serialization;

import com.google.gson.JsonObject;
import cx.rain.mc.catmessenger.message.IMessage;
import cx.rain.mc.catmessenger.message.type.EmptyMessage;
import cx.rain.mc.catmessenger.message.type.NewlineMessage;
import cx.rain.mc.catmessenger.message.type.TextMessage;
import cx.rain.mc.catmessenger.message.type.TranslatableMessage;

public class MessageTypes {

    public static IMessage tryJson(JsonObject json) {
        var type = json.getAsJsonPrimitive("type").getAsString();

        switch (type) {
            case "empty" -> {
                var message = new EmptyMessage();
                message.readJson(json);
                return message;
            }
            case "newline" -> {
                var message = new NewlineMessage();
                message.readJson(json);
                return message;
            }
            case "text" -> {
                var message = new TextMessage();
                message.readJson(json);
                return message;
            }
            case "translatable" -> {
                var message = new TranslatableMessage();
                message.readJson(json);
                return message;
            }
            default -> {
                return null;
            }
        }
    }
}
