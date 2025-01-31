package cx.rain.mc.catmessenger.connector;

@FunctionalInterface
public interface IMessageHandler {
    void handle(ConnectorMessage message);
}
