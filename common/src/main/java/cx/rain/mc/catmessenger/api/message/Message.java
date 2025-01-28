package cx.rain.mc.catmessenger.api.message;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Message(String platform, String content) {
        this(platform, null, content);
    }

    public Message(String platform, Player sender, String content) {
        this(platform, sender, content, ZonedDateTime.now());
    }

    /**
     * Displaying platform name.
     * Legacy format supported. (Use & as shift symbol, and &#FFFFFF for hex colors.)
     */
    String platform;

    /**
     * Displaying message sender.
     */
    @Nullable
    Player sender;

    /**
     * Displaying message content.
     * MiniMessage format.
     */
    String content;

    /**
     * Send time.
     */
    ZonedDateTime time;
}
