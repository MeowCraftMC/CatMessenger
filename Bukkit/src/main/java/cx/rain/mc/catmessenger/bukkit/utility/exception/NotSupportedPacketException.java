package cx.rain.mc.catmessenger.bukkit.utility.exception;

public class NotSupportedPacketException extends RuntimeException {
    public NotSupportedPacketException() {
        super();
    }

    public NotSupportedPacketException(Exception ex) {
        super(ex);
    }
}
