package cx.rain.mc.catmessenger.api.message;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * Player (message sender or platform user).
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Player(String id, UUID uuid) {
        this(id, uuid, null);
    }

    public Player(String id, UUID uuid, String name) {
        this(id, uuid, name, null, null);
    }

    /**
     * Sender id in the specific platform.
     * Literal string.
     * For Telegram: without the heading @.
     */
    String id;

    /**
     * Player uuid.
     * Null when on tg or player is a bot.
     */
    @Nullable
    UUID uuid;

    /**
     * Displaying name.
     * Legacy format supported. (Use & as shift symbol, and &#FFFFFF for hex colors.)
     */
    @Nullable
    String name;

    /**
     * Legacy format supported. (Use & as shift symbol, and &#FFFFFF for hex colors.)
     */
    @Nullable
    String prefix;

    /**
     * Legacy format supported. (Use & as shift symbol, and &#FFFFFF for hex colors.)
     */
    @Nullable
    String suffix;
}
