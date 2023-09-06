package cx.rain.mc.catmessenger.bukkit.handler;

import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.bukkit.utility.MessageSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventHandler implements Listener {
    private final ConfigManager config;

    public PlayerEventHandler(MessengerBukkit plugin) {
        config = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!config.broadcastSystemMessage()) {
            return;
        }

        var player = event.getPlayer();
        var name = player.getDisplayName();
        MessageSender.sendSystemMessage(player.getServer(), name + " 加入 " + config.getServerName() + " 服务器");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!config.broadcastSystemMessage()) {
            return;
        }

        var player = event.getPlayer();
        var name = player.getDisplayName();
        MessageSender.sendSystemMessage(player.getServer(), name + " 退出 " + config.getServerName() + " 服务器");
    }
}
