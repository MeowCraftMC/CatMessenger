package cx.rain.mc.catmessenger.bukkit.networking.payload;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;
import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;

public abstract class MessagePayload {
    public byte[] toBytes() {
        var writer = new CborWriter();
        writer.writeStartArray();
        writer.writeInt32(getType().getId());
        write(writer);
        writer.writeEndArray();
        return writer.getBytes();
    }

    protected abstract MessagePayloadType getType();

    public static void handle(String publisher, byte[] bytes) {
        var reader = new CborReader(bytes);
        reader.readStartArray();
        var type = reader.readInt32();

        MessagePayload payload = null;
        switch (MessagePayloadType.fromId(type)) {
            case RAW -> payload = new RawPayload(reader);
            case CHAT_COMPONENT -> payload = new ChatComponentPayload(reader);
            case CHAT_TEXT -> payload = new ChatTextPayload(reader);
            case SYSTEM -> payload = new SystemPayload(reader);
            case PLAYER_ONLINE -> payload = new PlayerOnlinePayload(reader);
            case SERVER_LIFECYCLE -> payload = new ServerLifecyclePayload(reader);
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

        if (payload != null && !publisher.equals(CatMessengerBukkit.getInstance().getConfigManager().getServerName())) {
            payload.handle(publisher);
        }

        reader.readEndArray();
    }

    protected abstract void write(CborWriter writer);

    protected abstract void handle(String publisher);

    protected MessagePayload() {
    }

    protected MessagePayload(CborReader reader) {
    }
}
