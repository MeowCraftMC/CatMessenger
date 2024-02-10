package cx.rain.mc.catmessenger.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cx.rain.mc.catmessenger.message.serialization.MessageTypes;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMessage implements IMessage {

    // <editor-fold desc="Decoration">

    private MessageColor color = null;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean strikethrough = false;
    private boolean spoiler = false;

    public MessageColor getColor() {
        return color;
    }

    public void setColor(MessageColor color) {
        this.color = color;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }

    // </editor-fold>

    // <editor-fold desc="Extra">

    private final List<IMessage> extras = new ArrayList<>();

    public boolean hasExtra() {
        return !extras.isEmpty();
    }

    public List<IMessage> getExtras() {
        return extras;
    }

    // </editor-fold>

    @Override
    public JsonObject writeJson() {
        var json = writeData();

        if (getColor() != null) {
            json.addProperty("color", getColor().asString());
        }
        if (isBold()) {
            json.addProperty("bold", true);
        }
        if (isItalic()) {
            json.addProperty("italic", true);
        }
        if (isUnderline()) {
            json.addProperty("underline", true);
        }
        if (isStrikethrough()) {
            json.addProperty("strikethrough", true);
        }
        if (isSpoiler()) {
            json.addProperty("spoiler", true);
        }

        if (hasExtra()) {
            var extras = new JsonArray();
            for (var extra : getExtras()) {
                extras.add(extra.writeJson());
            }
            json.add("extra", extras);
        }

        return json;
    }

    @Override
    public void readJson(JsonObject json) {
        readData(json);

        if (json.has("color")) {
            setColor(MessageColor.fromString(json.getAsJsonPrimitive("color").getAsString()));
        }
        if (json.has("bold")) {
            setBold(json.getAsJsonPrimitive("bold").getAsBoolean());
        }
        if (json.has("italic")) {
            setItalic(json.getAsJsonPrimitive("italic").getAsBoolean());
        }
        if (json.has("underline")) {
            setUnderline(json.getAsJsonPrimitive("underline").getAsBoolean());
        }
        if (json.has("strikethrough")) {
            setStrikethrough(json.getAsJsonPrimitive("strikethrough").getAsBoolean());
        }
        if (json.has("spoiler")) {
            setSpoiler(json.getAsJsonPrimitive("spoiler").getAsBoolean());
        }
        if (json.has("extra")) {
            for (var extra : json.getAsJsonArray("extra")) {
                if (extra instanceof JsonObject jsonObject) {
                    var message = MessageTypes.tryJson(jsonObject);
                    if (message != null) {
                        getExtras().add(message);
                    }
                }
            }
        }
    }

    public abstract JsonObject writeData();
    public abstract void readData(JsonObject json);
}
