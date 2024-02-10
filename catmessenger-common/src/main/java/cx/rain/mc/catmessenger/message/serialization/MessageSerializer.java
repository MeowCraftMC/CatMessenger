package cx.rain.mc.catmessenger.message.serialization;

import com.google.gson.*;
import cx.rain.mc.catmessenger.message.IMessage;

import java.lang.reflect.Type;

public class MessageSerializer implements IMessageSerializer<IMessage> {
    public static final MessageSerializer INSTANCE = new MessageSerializer();

    @Override
    public IMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonObject jsonObject) {
            return MessageTypes.tryJson(jsonObject);
        }
        throw new JsonParseException("Malformed message!");
    }

    @Override
    public JsonElement serialize(IMessage src, Type typeOfSrc, JsonSerializationContext context) {
        var json = src.writeJson();
        json.addProperty("type", src.getType());
        return json;
    }
}