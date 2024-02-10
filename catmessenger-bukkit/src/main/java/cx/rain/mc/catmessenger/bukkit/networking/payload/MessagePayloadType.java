package cx.rain.mc.catmessenger.bukkit.networking.payload;

public enum MessagePayloadType {
    RAW(0),
    CHAT_COMPONENT(1),
    CHAT_TEXT(2),
    SYSTEM(3),
    PLAYER_ONLINE(4),
    SERVER_LIFECYCLE(5),
    PLAYER_DEATH(6),
    PLAYER_ADVANCEMENT(7),
    QUERY_ONLINE(8),
    QUERY_TIME(9),
    RUN_COMMAND(10),
    QUERY_RESULT_ONLINE(11),
    QUERY_RESULT_TIME(12),
    COMMAND_RESULT(13),
    ;

    private final int id;

    MessagePayloadType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MessagePayloadType fromId(int id) {
        for (var v : values()) {
            if (v.id == id) {
                return v;
            }
        }
        return RAW;
    }
}
