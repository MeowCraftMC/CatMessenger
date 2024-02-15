package cx.rain.mc.catmessenger.connector;

import cx.rain.mc.catmessenger.message.AbstractMessage;

import java.time.ZonedDateTime;

public class ConnectorMessage {
    private String client;
    private AbstractMessage content;
    private AbstractMessage sender;
    private ZonedDateTime time;

    protected void setClient(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public void setSender(AbstractMessage sender) {
        this.sender = sender;
    }

    public AbstractMessage getSender() {
        return sender;
    }

    public void setContent(AbstractMessage content) {
        this.content = content;
    }

    public AbstractMessage getContent() {
        return content;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public ZonedDateTime getTime() {
        return time;
    }
}
