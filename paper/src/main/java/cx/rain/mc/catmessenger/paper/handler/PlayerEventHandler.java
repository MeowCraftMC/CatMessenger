package cx.rain.mc.catmessenger.paper.handler;

import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
import cx.rain.mc.catmessenger.api.utilities.MessageFactory;
import cx.rain.mc.catmessenger.paper.utility.MessengerHelper;
import cx.rain.mc.catmessenger.paper.utility.BukkitMessageHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        MessengerHelper.send(MessageFactory.playerJoined(BukkitMessageHelper.createPlayer(player)));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        MessengerHelper.send(MessageFactory.playerLeft(BukkitMessageHelper.createPlayer(player)));
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        var player = event.getPlayer();
        var display = event.getAdvancement().getDisplay();
        if (display != null) {
            MessengerHelper.send(BukkitMessageHelper.playerAdvancement(BukkitMessageHelper.createPlayer(player), display.title(), display.description(), display.frame()));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.isCancelled()) {
            return;
        }

        var message = ComponentSerializer.toPlain(event.deathMessage());
        var component = Component.text(message);
        MessengerHelper.send(component);
    }
}
