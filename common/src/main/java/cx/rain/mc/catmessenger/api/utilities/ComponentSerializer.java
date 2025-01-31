package cx.rain.mc.catmessenger.api.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class  ComponentSerializer {
    protected static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
    protected static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();
    protected static final GsonComponentSerializer JSON = GsonComponentSerializer.gson();
    protected static final MiniMessage MINI_MESSAGE = MiniMessage.builder().build();

    public static Component fromJson(String str) {
        return JSON.deserialize(str);
    }

    public static String toJson(Component component) {
        return JSON.serialize(component);
    }

    public static String toPlain(Component component) {
        return PLAIN.serialize(component);
    }
}
