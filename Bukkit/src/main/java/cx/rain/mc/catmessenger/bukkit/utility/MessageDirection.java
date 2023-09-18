package cx.rain.mc.catmessenger.bukkit.utility;

public enum MessageDirection {
    NONE(0),
    INCOMING(1),
    OUTGOING(2),
    ALL(3),
    ;

    private final int id;

    MessageDirection(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
