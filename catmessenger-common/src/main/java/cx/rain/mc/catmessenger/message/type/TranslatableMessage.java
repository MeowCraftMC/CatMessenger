package cx.rain.mc.catmessenger.message.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cx.rain.mc.catmessenger.message.AbstractMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranslatableMessage extends AbstractMessage {
    private String key = "";
    private List<String> args = new ArrayList<>();

    public TranslatableMessage() {
    }

    public TranslatableMessage(String key, String... args) {
        this.key = key;
        Collections.addAll(this.args, args);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getArgs() {
        return args;
    }

    public void addArg(String arg) {
        this.args.add(arg);
    }

    public void clearArg() {
        this.args.clear();
    }

    @Override
    public JsonObject writeData() {
        var json = new JsonObject();
        json.addProperty("key", getKey());

        if (!getArgs().isEmpty()) {
            var args = new JsonArray();
            for (var arg : getArgs()) {
                args.add(arg);
            }
            json.add("args", args);
        }

        return json;
    }

    @Override
    public void readData(JsonObject json) {
        setKey(json.get("key").getAsString());

        var args = json.getAsJsonArray("args");

        if (args == null || args.isEmpty()) {
            return;
        }

        for (var arg : args) {
            addArg(arg.getAsString());
        }
    }

    @Override
    public String getType() {
        return "translatable";
    }
}
