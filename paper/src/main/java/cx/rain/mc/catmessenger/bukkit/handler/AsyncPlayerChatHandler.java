package cx.rain.mc.catmessenger.bukkit.handler;

import cx.rain.mc.catmessenger.bukkit.utility.BrokerHelper;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncPlayerChatHandler implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncChatEvent event) {
        var player = event.getPlayer();
        var content = event.message();
        BrokerHelper.send(player, content);
    }
}
