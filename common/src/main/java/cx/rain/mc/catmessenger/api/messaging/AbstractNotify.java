package cx.rain.mc.catmessenger.api.messaging;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import cx.rain.mc.catmessenger.api.utilities.serializer.OffsetDateTimeTypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractNotify<MESSAGE> extends AbstractQueue {
    protected static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(OffsetDateTime.class, OffsetDateTimeTypeAdapter.INSTANCE)
            .setPrettyPrinting()
            .create();

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractNotify.class);

    protected final Class<MESSAGE> messageType;

    protected final List<IQueueHandler<MESSAGE>> handlers = new ArrayList<>();

    public AbstractNotify(String clientId, Supplier<Connection> connection, Class<MESSAGE> messageType) {
        super(clientId, connection);
        this.messageType = messageType;
    }

    @Override
    protected Consumer createConsumer() {
        return new NotifyConsumer<>(getChannel(), this);
    }

    @Override
    protected String getExchangeType() {
        return "fanout";
    }

    @Override
    protected String getRoutingKey() {
        return "";
    }

    public void publish(MESSAGE message) {
        Thread.startVirtualThread(() -> {
            var json = GSON.toJson(message);
            var bytes = json.getBytes(StandardCharsets.UTF_8);
            publish(bytes);
        });
    }

    public void handler(IQueueHandler<MESSAGE> handler) {
        handlers.add(handler);
    }

    @FunctionalInterface
    public interface IQueueHandler<MESSAGE> {
        void handle(@NotNull MESSAGE message);
    }

    private static class NotifyConsumer<M> extends DefaultConsumer {
        private final AbstractNotify<M> queue;

        public NotifyConsumer(Channel channel, AbstractNotify<M> queue) {
            super(channel);
            this.queue = queue;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                   byte[] body) throws IOException {
            Thread.startVirtualThread(() -> {
                if (queue.getClientId().equals(properties.getAppId())) {
                    queue.ack(envelope.getDeliveryTag());
                    return;
                }

                var str = new String(body, StandardCharsets.UTF_8);
                var message = GSON.fromJson(str, queue.messageType);

                if (message == null) {
                    queue.ack(envelope.getDeliveryTag());
                    return;
                }

                for (var handler : queue.handlers) {
                    try {
                        handler.handle(message);
                    } catch (Exception ex) {
                        LOGGER.error("Error handling message: \n{}\n{}", message, ex);
                    }
                }

                queue.ack(envelope.getDeliveryTag());
            });
        }
    }
}
