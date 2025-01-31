package cx.rain.mc.catmessenger.message.type;

import com.google.gson.JsonObject;
import cx.rain.mc.catmessenger.message.AbstractMessage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TextMessage extends AbstractMessage {
    private String text = "";

    public TextMessage() {
    }

    public TextMessage(String text) {
        this.text = text;
    }

    @Override
    public JsonObject writeData() {
        var json = new JsonObject();
        json.addProperty("text", getText());
        return json;
    }

    @Override
    public void readData(JsonObject json) {
        setText(json.get("text").getAsString());
    }

    @Override
    public String getType() {
        return "text";
    }
}
