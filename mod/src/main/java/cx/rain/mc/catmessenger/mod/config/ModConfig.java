package cx.rain.mc.catmessenger.mod.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ModConfig implements Serializable {
    private String id = "exampleModServer";

    private String name = "Example Mod Platform";

    private RabbitMQConfig rabbitMQ = new RabbitMQConfig();

    @Getter
    @Setter
    public static class RabbitMQConfig implements Serializable {
        private String host = "localhost";

        private int port = 5672;

        private String username = "guest";

        private String password = "guest";

        private String virtualHost = "/minecraft";

        private int maxRetry = 5;

        private long retryIntervalMillis = 500;
    }
}
