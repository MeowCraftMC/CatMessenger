package cx.rain.mc.catmessenger.api.model;

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
     * Sender id in the specific platform. <br/>
     * Literal string. <br/>
     * For Telegram: without the heading @.
     */
    String id;

    /**
     * Player uuid. <br/>
     * Null when on tg or player is a bot.
     */
    @Nullable
    UUID uuid;

    /**
     * Displaying name. <br/>
     *
     * @see Message#content
     */
    @Nullable
    String name;

    /**
     * Json format.
     *
     * @see Message#content
     */
    @Nullable
    String prefix;

    /**
     * Json format.
     *
     * @see Message#content
     */
    @Nullable
    String suffix;
}
