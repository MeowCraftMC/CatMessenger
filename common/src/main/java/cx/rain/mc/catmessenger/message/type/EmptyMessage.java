package cx.rain.mc.catmessenger.message.type;

import com.google.gson.JsonObject;
import cx.rain.mc.catmessenger.message.AbstractMessage;

public class EmptyMessage extends AbstractMessage {
    public EmptyMessage() {
    }

    @Override
    public JsonObject writeData() {
        return new JsonObject();
    }

    @Override
    public void readData(JsonObject json) {
    }

    @Override
    public String getType() {
        return "empty";
    }
}
