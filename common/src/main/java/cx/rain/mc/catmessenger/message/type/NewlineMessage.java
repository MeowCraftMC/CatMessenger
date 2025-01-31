package cx.rain.mc.catmessenger.message.type;

import com.google.gson.JsonObject;
import cx.rain.mc.catmessenger.message.AbstractMessage;

public class NewlineMessage extends AbstractMessage {
    public NewlineMessage() {
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
        return "newline";
    }
}
