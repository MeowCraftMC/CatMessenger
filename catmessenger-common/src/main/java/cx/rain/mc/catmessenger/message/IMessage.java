package cx.rain.mc.catmessenger.message;

import com.google.gson.JsonObject;

import java.util.List;

public interface IMessage {
    String getType();

    boolean hasExtra();
    List<IMessage> getExtras();

    void readJson(JsonObject json);
    JsonObject writeJson();
}
