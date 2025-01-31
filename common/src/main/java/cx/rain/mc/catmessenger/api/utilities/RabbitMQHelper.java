package cx.rain.mc.catmessenger.api.utilities;

public class RabbitMQHelper {
    public static String getMessageExchange() {
        return "fanout.exchange.messages";
    }

    public static String getMessageQueue(String clientId) {
        return "fanout.queue." + clientId;
    }

    public static String getCommandExchange() {
        return "topic.exchange.commands";
    }

    public static String getCommandQueue(String clientId) {
        return "topic.queue." + clientId;
    }

    public static String getCommandRoutingKey(String clientId) {
        return clientId;
    }
}
