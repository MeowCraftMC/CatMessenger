package cx.rain.mc.catmessenger.connector;

import cx.rain.mc.catmessenger.message.IMessage;

@FunctionalInterface
public interface IMessageHandler {
    void handle(IMessage message, String sender);
}
