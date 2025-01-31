package cx.rain.mc.catmessenger.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cx.rain.mc.catmessenger.message.serialization.MessageTypes;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractMessage {

    // <editor-fold desc="Decoration">

    @Setter
    private MessageColor color = MessageColor.RESET;
    @Setter
    private boolean bold = false;
    @Setter
    private boolean italic = false;
    @Setter
    private boolean underline = false;
    @Setter
    private boolean strikethrough = false;
    @Setter
    private boolean spoiler = false;

    // </editor-fold>

    // <editor-fold desc="Extra">

    private final List<AbstractMessage> extras = new ArrayList<>();

    public boolean hasExtra() {
        return !extras.isEmpty();
    }

    // </editor-fold>

    // <editor-fold desc="Interactable">

    //        if (isSpoiler()) {
    //            throw new IllegalStateException("Don't combine hover and spoiler.");
    //        }
    @Setter
    private AbstractMessage hoverMessage = null;

    public boolean hasHoverMessage() {
        return !isSpoiler() && hoverMessage != null;
    }

    private ClickEvent clickEvent = null;
    private String clickValue = null;

    public boolean hasClickEvent() {
        return clickEvent != null && clickValue != null;
    }

    public void setClickEvent(ClickEvent clickEvent, String value) {
        this.clickEvent = clickEvent;
        this.clickValue = value;
    }

    // </editor-fold>

    // <editor-fold desc="Serialization">

    public JsonObject writeJson() {
        var json = writeData();

        json.addProperty("type", getType());

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

        if (hasHoverMessage()) {
            json.add("hover", getHoverMessage().writeJson());
        }
        if (hasClickEvent()) {
            json.addProperty("clickEvent", getClickEvent().getName());
            json.addProperty("clickValue", getClickValue());
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

        if (json.has("hover") && !isSpoiler()) {
            var hoverJson = json.getAsJsonObject("hover");
            var hover = MessageTypes.tryJson(hoverJson);
            setHoverMessage(hover);
        }
        if (json.has("clickEvent") && json.has("clickValue")) {
            var eventStr = json.getAsJsonPrimitive("clickEvent").getAsString();
            var value = json.getAsJsonPrimitive("clickValue").getAsString();

            var event = ClickEvent.of(eventStr);
            if (event != null) {
                setClickEvent(event, value);
            }
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

    // </editor-fold>

    // <editor-fold desc="Abstract methods">

    public abstract JsonObject writeData();
    public abstract void readData(JsonObject json);

    public abstract String getType();

    // </editor-fold>
}
