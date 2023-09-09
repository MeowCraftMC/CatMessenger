package cx.rain.mc.catmessenger.bungee.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import cx.rain.mc.catmessenger.bungee.MessengerBungee;
import okhttp3.OkHttpClient;

public class Bot {
    private final TelegramBot bot;

    public Bot(String token, OkHttpClient okHttpClient) {
        bot = new TelegramBot.Builder(token)
                .okHttpClient(okHttpClient)
                .build();
    }

    public void start() {
        bot.setUpdatesListener(new MessageUpdateListener(), Throwable::printStackTrace);
    }

    public void stop() {
        bot.removeGetUpdatesListener();
    }

    public String getChatId() {
        return MessengerBungee.getInstance().getConfigManager().getGroupId();
    }

    public String getMessageFormat() {
        return MessengerBungee.getInstance().getConfigManager().getMessageFormat();
    }

    public String getSystemMessageFormat() {
        return MessengerBungee.getInstance().getConfigManager().getSystemMessageFormat();
    }

    public void sendMessage(String sender, String content) {
        var message = "";
        if (sender.isBlank()) {
            message = String.format(getSystemMessageFormat(), content);
        } else {
            message = String.format(getMessageFormat(), sender, content);
        }

        bot.execute(new SendMessage(getChatId(), message).parseMode(ParseMode.HTML), MessageSendCallback.SEND_MESSAGE_CALLBACK);
    }
}
