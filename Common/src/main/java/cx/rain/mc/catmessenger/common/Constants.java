package cx.rain.mc.catmessenger.common;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cx.rain.mc.catmessenger.common.message.AbstractMessage;
import cx.rain.mc.catmessenger.common.message.serialization.MessageSerializer;

public class Constants {
    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(AbstractMessage.class, MessageSerializer.INSTANCE)
            .create();
}
