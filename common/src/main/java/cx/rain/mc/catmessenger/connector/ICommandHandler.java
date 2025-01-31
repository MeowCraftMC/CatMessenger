package cx.rain.mc.catmessenger.connector;

import com.rabbitmq.client.AMQP;

@FunctionalInterface
public interface ICommandHandler {
    void handle(ConnectorCommand message, AMQP.BasicProperties props);
}
