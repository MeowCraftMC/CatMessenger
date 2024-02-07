package cx.rain.mc.catmessenger.common.message;

import com.google.gson.JsonObject;

public interface IMessage {
    String getType();

    void readJson(JsonObject json);
    JsonObject writeJson();
}
