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
     * Json format.
     *
     * @see Message#content
     */
    String platform;

    /**
     * Displaying message sender.
     */
    @Nullable Player sender;

    /**
     * Displaying message content. <br/>
     * Json format.
     *
     * @see <a href="https://zh.minecraft.wiki/w/%E6%96%87%E6%9C%AC%E7%BB%84%E4%BB%B6">Text Component - Minecraft Wiki</a>
     */
    String content;

    /**
     * Send time.
     */
    OffsetDateTime time;
}
