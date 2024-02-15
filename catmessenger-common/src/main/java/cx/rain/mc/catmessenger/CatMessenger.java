package cx.rain.mc.catmessenger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cx.rain.mc.catmessenger.message.AbstractMessage;
import cx.rain.mc.catmessenger.message.serialization.MessageSerializer;
import cx.rain.mc.catmessenger.utility.ZonedDateTimeTypeAdapter;

import java.time.ZonedDateTime;

public class CatMessenger {
    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(AbstractMessage.class, MessageSerializer.INSTANCE)
            .registerTypeAdapter(ZonedDateTime.class, ZonedDateTimeTypeAdapter.INSTANCE)
            .create();
}
