package cx.rain.mc.catmessenger.api.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ComponentSerializer {
    protected static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
    protected static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();
    protected static final MiniMessage MINI_MESSAGE = MiniMessage.builder().build();

    public static Component fromMiniMessage(String str) {
        return MINI_MESSAGE.deserialize(str);
    }

    public static Component fromLegacy(String str) {
        return LEGACY.deserialize(str);
    }

    public static String toLegacy(Component component) {
        return LEGACY.serialize(component);
    }

    public static String toMiniMessage(Component component) {
        return MINI_MESSAGE.serialize(component);
    }

    public static String toPlain(Component component) {
        return PLAIN.serialize(component);
    }
}
