package cx.rain.mc.catmessenger.bukkit.handler;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.MessageHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventHandler implements Listener {

    public PlayerEventHandler() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var name = player.getDisplayName();

        CatMessengerBukkit.getInstance().getConnector().publish(MessageHelper.buildJoinMessage(name));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        var name = player.getDisplayName();

        CatMessengerBukkit.getInstance().getConnector().publish(MessageHelper.buildQuitMessage(name));
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        var player = event.getPlayer();
        var name = player.getDisplayName();
        var advancement = event.getAdvancement();
        var title = advancement.getDisplay().getTitle();
        var description = advancement.getDisplay().getDescription();
        var type = advancement.getDisplay().getType();

        CatMessengerBukkit.getInstance().getConnector().publish(MessageHelper
                .buildAdvancementMessage(name, title, description, type));
    }
}