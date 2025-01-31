package cx.rain.mc.catmessenger.api.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

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
        this(platform, sender, content, OffsetDateTime.now());
    }

    /**
     * Displaying platform name. <br/>
     * Legacy format. See {@link Message#content}
     */
    String platform;

    /**
     * Displaying message sender.
     */
    @Nullable
    Player sender;

    /**
     * Displaying message content. <br/>
     * Legacy format. (Use & as shift symbol, and &#FFFFFF for hex colors.)
     */
    String content;

    /**
     * Send time.
     */
    OffsetDateTime time;
}
