package cx.rain.mc.catmessenger.common.message.serialization;

import com.google.gson.JsonObject;
import cx.rain.mc.catmessenger.common.message.IMessage;
import cx.rain.mc.catmessenger.common.message.type.TextMessage;

public class MessageTypes {

    public static IMessage tryJson(JsonObject json) {
        var type = json.getAsJsonPrimitive("type").getAsString();

        switch (type) {
            case "text" -> {
                var message = new TextMessage();
                message.readJson(json);
                return message;
            }
            default -> {
                return null;
            }
        }
    }
}
