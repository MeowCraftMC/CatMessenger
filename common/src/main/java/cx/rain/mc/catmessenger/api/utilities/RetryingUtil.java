package cx.rain.mc.catmessenger.api.utilities;

import java.util.function.BiConsumer;

public class RetryingUtil {
    public static void runWithRetry(Task runnable, int maxRetry, Runnable onSucceed,
                                    BiConsumer<Throwable, Integer> onFailed, Runnable onAllAttemptFailed) {
        var tries = 0;
        while (tries <= maxRetry) {
            try {
                runnable.run();
                onSucceed.run();
                return;
            } catch (Throwable t) {
                onFailed.accept(t, tries);
                tries += 1;
            }
        }
        onAllAttemptFailed.run();
    }

    @FunctionalInterface
    public interface Task {
        void run() throws Throwable;
    }
}
