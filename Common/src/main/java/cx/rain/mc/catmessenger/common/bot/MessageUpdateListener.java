package cx.rain.mc.catmessenger.common.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

public class MessageUpdateListener implements UpdatesListener {
    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            processUpdate(update);
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {

    }
}
