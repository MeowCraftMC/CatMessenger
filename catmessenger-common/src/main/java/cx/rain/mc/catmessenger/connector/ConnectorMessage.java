package cx.rain.mc.catmessenger.connector;

import cx.rain.mc.catmessenger.message.IMessage;

public class ConnectorMessage {
    private IMessage message;
    private String sender;

    public void setMessage(IMessage message) {
        this.message = message;
    }

    public IMessage getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
