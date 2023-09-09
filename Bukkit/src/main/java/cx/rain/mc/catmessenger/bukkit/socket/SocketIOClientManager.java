package cx.rain.mc.catmessenger.bukkit.socket;

import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.common.Constants;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;

import java.net.URISyntaxException;

public class SocketIOClientManager {
    private final ConfigManager configManager;

    private final Socket socket;

    public SocketIOClientManager(MessengerBukkit plugin) {
        configManager = plugin.getConfigManager();

        try {
            socket = IO.socket(configManager.getSocketURL());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        socket.on(Constants.EVENT_MESSAGE, args -> {
            if (args.length != 5) {
                return;
            }

            var platform = args[0].toString();
            var from = args[1].toString();
            var sender = args[2].toString();
            var time = args[3].toString();
            var content = args[4].toString();

            if (!platform.equalsIgnoreCase(Constants.CHANNEL_PLATFORM_TELEGRAM)) {
                return;
            }

            if (from.equals(configManager.getServerName())) {
                return;
            }

            var component = new TextComponent();

            if (sender.isBlank()) { // Todo: qyl27: Do not forward player death?
                if (!configManager.showSystemMessage()) {
                    return;
                }
            } else {
                var senderComponent = new TextComponent("[" + sender + "] ");
                senderComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("时间：" + time)));
                senderComponent.setColor(ChatColor.GREEN);
                component.addExtra(senderComponent);
            }

            var trimLength = configManager.getTrimLength();
            if (trimLength != -1 && content.length() > trimLength) {
                var trim = new TextComponent(content.substring(0, trimLength));
                var fullText = new TextComponent("[全文]");   // Todo: qyl27: I18n support.
                fullText.setColor(ChatColor.GOLD);
                fullText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(content)));
                trim.addExtra(fullText);
                component.addExtra(trim);
            } else {
                component.addExtra(new TextComponent(content));
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Bukkit.getServer().spigot().broadcast(component);
            });
        });
    }

    public void start() {
        socket.connect();
    }

    public void stop() {
        socket.disconnect();
    }

    public void sendMessage(String platform, String from, String sender, String time, String content) {
        socket.emit(Constants.EVENT_MESSAGE, platform, from, sender, time, content);
    }
}
