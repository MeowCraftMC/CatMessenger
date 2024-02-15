package cx.rain.mc.catmessenger.bukkit.handler;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.MessageHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
        var display = advancement.getDisplay();

        if (display == null) {
            return;
        }

        var title = display.getTitle();
        var description = display.getDescription();
        var type = display.getType();

        CatMessengerBukkit.getInstance().getConnector().publish(MessageHelper
                .buildAdvancementMessage(name, title, description, type));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        var name = player.getDisplayName();
        var message = event.getDeathMessage();
        CatMessengerBukkit.getInstance().getConnector().publish(MessageHelper.buildPlayerDeathMessage(name, message));
    }
}
