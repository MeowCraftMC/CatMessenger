package cx.rain.mc.catmessenger.mod.config;

public class ModConfig {
    public String name = "ExampleModdedMinecraftServer";
    public ConnectorConfig connector = new ConnectorConfig();

    public static class ConnectorConfig {
        public String host = "localhost";
        public int port = 5672;
        public String username = "guest";
        public String password = "guest";
        public String virtualHost = "/minecraft";
        public int maxRetry = 5;
    }
}
