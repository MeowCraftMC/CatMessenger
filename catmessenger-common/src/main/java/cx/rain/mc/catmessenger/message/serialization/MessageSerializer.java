package cx.rain.mc.catmessenger.message.serialization;

import com.google.gson.*;
import cx.rain.mc.catmessenger.message.AbstractMessage;

import java.lang.reflect.Type;

public class MessageSerializer implements JsonSerializer<AbstractMessage>, JsonDeserializer<AbstractMessage> {
    public static final MessageSerializer INSTANCE = new MessageSerializer();

    @Override
    public AbstractMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonObject jsonObject) {
            return MessageTypes.tryJson(jsonObject);
        }
        throw new JsonParseException("Malformed message!");
    }

    @Override
    public JsonElement serialize(AbstractMessage src, Type typeOfSrc, JsonSerializationContext context) {
        var json = src.writeJson();
        json.addProperty("type", src.getType());
        return json;
    }
}
