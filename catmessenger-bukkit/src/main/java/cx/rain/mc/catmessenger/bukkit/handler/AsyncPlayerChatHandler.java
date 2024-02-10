package cx.rain.mc.catmessenger.bukkit.handler;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.networking.payload.ChatTextPayload;
import cx.rain.mc.catmessenger.bukkit.utility.MessageSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatHandler implements Listener {
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();

        var name = player.getDisplayName();
        var content = event.getMessage();

        CatMessengerBukkit.getInstance().getConnectorClient().send(new ChatTextPayload(name, content));
    }
}
