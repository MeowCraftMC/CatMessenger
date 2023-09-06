package cx.rain.mc.catmessenger.bungee.bot;

import cx.rain.mc.catmessenger.common.bot.Bot;

public class BotManager {
    private Bot bot;

    public BotManager(String token) {
        bot = new Bot(token);
    }
}
