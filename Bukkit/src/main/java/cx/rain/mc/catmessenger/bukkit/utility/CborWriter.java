package cx.rain.mc.catmessenger.bukkit.utility;

import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;

import java.io.*;

public class CborWriter {
    private static final CBORFactory FACTORY = CBORFactory.builder().build();

    private final ByteArrayOutputStream out;
    private final CBORGenerator generator;

    public CborWriter() {
        out = new ByteArrayOutputStream();
        try {
            generator = FACTORY.createGenerator(out);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeStartArray() {
        try {
            generator.writeStartArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeEndArray() {
        try {
            generator.writeEndArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeString(String value) {
        try {
            generator.writeString(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeInt32(int value) {
        try {
            generator.writeNumber(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeBoolean(boolean value) {
        try {
            generator.writeBoolean(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeBytes(byte[] value) {
        try {
            generator.writeBinary(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] getBytes() {
        return out.toByteArray();
    }
}
