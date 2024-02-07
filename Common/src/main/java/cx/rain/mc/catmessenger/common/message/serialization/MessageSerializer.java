package cx.rain.mc.catmessenger.common.message.serialization;

import com.google.gson.*;
import cx.rain.mc.catmessenger.common.message.IMessage;

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
        return src.writeJson();
    }
}
