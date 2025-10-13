package cx.rain.mc.catmessenger.api.utilities;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RetryingUtil {
    public static void runWithRetry(Supplier<Boolean> runnable, int maxRetry, Runnable onSucceed,
                                    Consumer<Integer> onFailed, Runnable onAllAttemptFailed) {
        var tries = 0;
        while (tries <= maxRetry) {
            if (runnable.get()) {
                onSucceed.run();
                return;
            }
            onFailed.accept(tries);
            tries += 1;
        }
        onAllAttemptFailed.run();
    }
}
