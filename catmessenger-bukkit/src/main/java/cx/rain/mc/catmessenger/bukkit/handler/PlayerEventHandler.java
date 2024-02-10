package cx.rain.mc.catmessenger.bukkit.handler;

import cx.rain.mc.catmessenger.bukkit.CatMessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.networking.payload.PlayerOnlinePayload;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventHandler implements Listener {
    private final ConfigManager config;

    public PlayerEventHandler(CatMessengerBukkit plugin) {
        config = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!config.broadcastSystemMessage()) {
            return;
        }

        var player = event.getPlayer();
        var name = player.getDisplayName();

        CatMessengerBukkit.getInstance().getConnectorClient().send(new PlayerOnlinePayload(true, name));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!config.broadcastSystemMessage()) {
            return;
        }

        var player = event.getPlayer();
        var name = player.getDisplayName();
        CatMessengerBukkit.getInstance().getConnectorClient().send(new PlayerOnlinePayload(false, name));
    }
}
