package cx.rain.mc.catmessenger.paper.handler;

import cx.rain.mc.catmessenger.paper.utility.MessengerHelper;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncPlayerChatHandler implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncChatEvent event) {
        var player = event.getPlayer();
        var content = event.message();
        MessengerHelper.send(player, content);
    }
}
