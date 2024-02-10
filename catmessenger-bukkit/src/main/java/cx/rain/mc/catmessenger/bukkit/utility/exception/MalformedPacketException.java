package cx.rain.mc.catmessenger.bukkit.utility.exception;

public class MalformedPacketException extends RuntimeException {
    public MalformedPacketException() {
        super();
    }

    public MalformedPacketException(Exception ex) {
        super(ex);
    }
}
