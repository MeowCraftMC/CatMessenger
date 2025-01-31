package cx.rain.mc.catmessenger.api.messaging;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractRPC extends AbstractQueue {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRPC.class);

    protected final Map<String, IMessage<IRequest, IResponse>> messagesById = new HashMap<>();
    protected final Map<Class<IRequest>, IMessage<IRequest, IResponse>> messagesByRequest = new HashMap<>();

    public AbstractRPC(String id, Supplier<Connection> connection) {
        super(id, connection);
    }

    protected abstract Gson getGson();

    @Override
    protected Consumer createConsumer() {
        return new RpcConsumer(getChannel(), this);
    }

    @Override
    protected String getExchangeType() {
        return "topic";
    }

    public void send(IRequest message) {
        // Todo: build message.

    }

    public void register(String messageId, IMessage<IRequest, IResponse> message) {
        messagesById.put(messageId, message);
        messagesByRequest.put(message.requestType, message);
    }

    public interface IRequest {
    }

    public interface IResponse {
    }

    @FunctionalInterface
    public interface IHandler<REQ extends IRequest, RES extends IResponse> {
        @NotNull RES handle(@NotNull REQ message);
    }

    @Builder
    @AllArgsConstructor
    public static class IMessage<REQ extends IRequest, RES extends IResponse> {
        private Class<REQ> requestType;
        private Class<RES> responseType;
        private IHandler<REQ, RES> handler;
    }

    private static class RpcConsumer extends DefaultConsumer {

        private final AbstractRPC queue;

        public RpcConsumer(Channel channel, AbstractRPC queue) {
            super(channel);
            this.queue = queue;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                   byte[] body) throws IOException {
            Thread.startVirtualThread(() -> {
                var str = new String(body, StandardCharsets.UTF_8);
                // Todo: Deserialize id and args.
//                var message = queue.getGson().fromJson(str, queue.messageType);
//
//                if (message == null) {
//                    queue.ack(envelope.getDeliveryTag());
//                    return;
//                }
//
//                for (var handler : queue.handlers) {
//                    try {
//                        handler.handle(message);
//                    } catch (Exception ex) {
//                        LOGGER.error("Error handling message: \n{}\n{}", message, ex);
//                    }
//                }
//                queue.ack(envelope.getDeliveryTag());
            });
        }
    }
}
