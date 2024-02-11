package cx.rain.mc.catmessenger.matrix;

public class Main {
    public static void main(String[] args) {
        var client = new CatMessengerMatrix();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Bye~");
            client.stop();
        }));

        client.start();

        while (true) {
        }
    }
}
