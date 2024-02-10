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
        if (!(token instanceof CTInteger integer)) {
            throw new MalformedPacketException();
        }
        return integer.getValue().intValue();
    }

    public boolean readBoolean() {
        var token = nextToken();
        if (!(token instanceof CTBoolean bool)) {
            throw new MalformedPacketException();
        }
        return bool.getValue();
    }

    public byte[] readBytes() {
        var token = nextToken();
        if (!(token instanceof CTByteString bytes)) {
            throw new MalformedPacketException();
        }
        return bytes.getValue();
    }
}
