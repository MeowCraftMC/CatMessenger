package cx.rain.mc.catmessenger.api.utilities.serializer;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.OffsetDateTime;

public class OffsetDateTimeTypeAdapter extends TypeAdapter<OffsetDateTime> {
    public static final OffsetDateTimeTypeAdapter INSTANCE = new OffsetDateTimeTypeAdapter();

    @Override
    public void write(JsonWriter out, OffsetDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.toString());
    }

    @Override
    public OffsetDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return OffsetDateTime.parse(in.nextString());
    }
}
