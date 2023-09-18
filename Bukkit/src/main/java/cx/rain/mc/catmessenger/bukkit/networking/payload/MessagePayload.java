package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;

public abstract class MessagePayload {
    public byte[] toBytes() {
        var writer = new CborWriter();
        write(writer);
        return writer.getBytes();
    }

    public static void handle(String publisher, byte[] bytes) {
        var reader = new CborReader(bytes);
        var type = reader.readInt32();

        MessagePayload payload = null;
        switch (MessagePayloadType.fromId(type)) {
            case RAW -> {
                payload = new RawPayload(reader);
            }
            case CHAT_COMPONENT -> {
                payload = new ChatComponentPayload(reader);
            }
            case CHAT_TEXT -> {
            }
            case SYSTEM -> {
            }
            case PLAYER_ONLINE -> {
            }
            case SERVER_LIFECYCLE -> {
            }
            case PLAYER_DEATH -> {
            }
            case PLAYER_ADVANCEMENT -> {
            }
            case QUERY_ONLINE -> {
            }
            case QUERY_TIME -> {
            }
            case RUN_COMMAND -> {
            }
            case QUERY_RESULT_ONLINE -> {
            }
            case QUERY_RESULT_TIME -> {
            }
            case COMMAND_RESULT -> {
            }
        }

        if (payload != null) {
            payload.handle(publisher);
        }
    }

    protected abstract void write(CborWriter writer);

    protected abstract void handle(String publisher);

    protected MessagePayload() {
    }

    protected MessagePayload(CborReader reader) {
    }
}
