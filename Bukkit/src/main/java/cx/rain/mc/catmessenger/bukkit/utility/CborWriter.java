package cx.rain.mc.catmessenger.bukkit.utility;

import com.authlete.cbor.CBOROutputStream;

import java.io.*;
import java.util.Base64;

public class CborWriter {
    private final ByteArrayOutputStream out;
    private final CBOROutputStream cbor;

    public CborWriter() {
        out = new ByteArrayOutputStream();
        cbor = new CBOROutputStream(out);
    }

    public void writeStartArray() {
        try {
            cbor.write(0x9f);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeEndArray() {
        try {
            cbor.write(0xff);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeString(String value) {
        try {
            cbor.writeString(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeInt32(int value) {
        try {
            cbor.writeInteger(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeBoolean(boolean value) {
        try {
            cbor.writeBoolean(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeBytes(byte[] value) {
        try {
            cbor.writeByteArray(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] getBytes() {
        return out.toByteArray();
    }
}
