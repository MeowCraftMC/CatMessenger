package cx.rain.mc.catmessenger.common.message.serialization;

import com.google.gson.*;

public interface IMessageSerializer<T> extends JsonSerializer<T>, JsonDeserializer<T> {
}
