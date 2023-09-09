package cx.rain.mc.catmessenger.bungee.http;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OkHttpRetryInterceptor implements Interceptor {
    private final int maxRetry;

    public OkHttpRetryInterceptor(int retry) {
        maxRetry = retry;
        // Todo: qyl27: retry count from config file.
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var request = chain.request();
        var response = chain.proceed(request);

        var retryCount = 1;
        while (!response.isSuccessful() && retryCount < maxRetry) {
            retryCount += 1;
            response.close();
            response = chain.proceed(request);
        }

        return response;
    }
}
