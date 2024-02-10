package cx.rain.mc.catmessenger.message.serialization;

import com.google.gson.*;

public interface IMessageSerializer<T> extends JsonSerializer<T>, JsonDeserializer<T> {
}
