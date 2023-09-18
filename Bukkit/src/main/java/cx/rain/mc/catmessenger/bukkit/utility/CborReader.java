package cx.rain.mc.catmessenger.bukkit.utility;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import cx.rain.mc.catmessenger.bukkit.utility.exception.MalformedPacketException;

import java.io.IOException;

public class CborReader {
    private static final CBORFactory FACTORY = CBORFactory.builder().build();

    private final CBORParser parser;

    public CborReader(byte[] bytes) {
        try {
            parser = FACTORY.createParser(bytes);
        } catch (IOException ex) {
            throw new MalformedPacketException(ex);
        }
    }

    public void readStartArray() {
        try {
            var result = parser.getCurrentToken();
            parser.nextToken();
            if (result != JsonToken.START_ARRAY) {
                throw new MalformedPacketException();
            }
        } catch (IOException ex) {
            throw new MalformedPacketException(ex);
        }
    }

    public void readEndArray() {
        try {
            var result = parser.getCurrentToken() == JsonToken.END_ARRAY;
            parser.nextToken();
            if (!result) {
                throw new MalformedPacketException();
            }
        } catch (IOException ex) {
            throw new MalformedPacketException(ex);
        }
    }

    public String readString() {
        try {
            var result = parser.getValueAsString();
            parser.nextToken();
            if (result == null) {
                throw new MalformedPacketException();
            }

            return result;
        } catch (IOException ex) {
            throw new MalformedPacketException(ex);
        }
    }

    public int readInt32() {
        try {
            var result = parser.getIntValue();
            parser.nextToken();
            return result;
        } catch (IOException ex) {
            throw new MalformedPacketException(ex);
        }
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
