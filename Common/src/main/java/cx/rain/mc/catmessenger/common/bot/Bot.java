package cx.rain.mc.catmessenger.common.bot;

import com.pengrad.telegrambot.TelegramBot;
import okhttp3.OkHttpClient;

public class Bot {
    private final TelegramBot bot;

    public Bot(String token, OkHttpClient okHttpClient) {
        bot = new TelegramBot.Builder(token)
                .okHttpClient(okHttpClient)
                .build();

        bot.setUpdatesListener(new MessageUpdateListener(), Throwable::printStackTrace);
    }

    public void stop() {
        bot.removeGetUpdatesListener();
    }
}
