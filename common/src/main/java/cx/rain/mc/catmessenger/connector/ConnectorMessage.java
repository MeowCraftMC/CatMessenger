package cx.rain.mc.catmessenger.connector;

import cx.rain.mc.catmessenger.message.AbstractMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ConnectorMessage {
    private String client;
    private AbstractMessage content;
    private AbstractMessage sender;
    private ZonedDateTime time;
}
