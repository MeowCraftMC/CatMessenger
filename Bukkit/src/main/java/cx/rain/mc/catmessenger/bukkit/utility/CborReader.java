package cx.rain.mc.catmessenger.bukkit.utility;

import com.authlete.cbor.token.*;
import cx.rain.mc.catmessenger.bukkit.utility.exception.MalformedPacketException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CborReader {
    private final CBORTokenizer decoder;

    private int currentIndex = 0;

    public CborReader(byte[] bytes) {
        decoder = new CBORTokenizer(new ByteArrayInputStream(bytes));
    }

    private CBORToken<?> nextToken() {
        try {
            return decoder.next();
        } catch (IOException ex) {
            throw new MalformedPacketException(ex);
        }
    }

    public void readStartArray() {
        var token = nextToken();
        if (!(token instanceof CTIndefiniteArrayOpener)) {
            throw new MalformedPacketException();
        }
    }

    public void readEndArray() {
        var token = nextToken();
        if (!(token instanceof CTBreak)) {
            throw new MalformedPacketException();
        }
    }

    public String readString() {
        var token = nextToken();
        if (!(token instanceof CTTextString stringToken)) {
            throw new MalformedPacketException();
        }
        return stringToken.getValue();
    }

    public int readInt32() {
        var token = nextToken();
        if (!(token instanceof CTInteger stringToken)) {
            throw new MalformedPacketException();
        }
        return stringToken.getValue();
    }

    public boolean readBoolean() {
        try {
            var result = parser.getBooleanValue();
            parser.nextToken();
            return result;
        } catch (IOException ex) {
            throw new MalformedPacketException(ex);
        }
    }

    public byte[] readBytes() {
        try {
            var result = parser.getBinaryValue();
            parser.nextToken();
            return result;
        } catch (IOException ex) {
            throw new MalformedPacketException(ex);
        }
    }
}
