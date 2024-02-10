package cx.rain.mc.catmessenger.message;

import com.google.gson.JsonObject;

public interface IMessage {
    String getType();

    void readJson(JsonObject json);
    JsonObject writeJson();
}
