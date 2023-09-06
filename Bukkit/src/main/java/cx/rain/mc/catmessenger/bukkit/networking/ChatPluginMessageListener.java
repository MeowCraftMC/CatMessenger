package cx.rain.mc.catmessenger.bukkit.networking;

import com.google.common.io.ByteStreams;
import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.config.ConfigManager;
import cx.rain.mc.catmessenger.common.CatMessenger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class ChatPluginMessageListener implements PluginMessageListener {
    private final ConfigManager config;

    public ChatPluginMessageListener(MessengerBukkit plugin) {
        config = plugin.getConfigManager();
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(CatMessenger.MESSAGES_CHANNEL_NAME)) {
            return;
        }

        var input = ByteStreams.newDataInput(message);
        var platform = input.readUTF();
        var from = input.readUTF();
        var sender = input.readUTF();
        var time = input.readUTF();
        var content = input.readUTF();

        if (!platform.equalsIgnoreCase(CatMessenger.CHANNEL_PLATFORM_TELEGRAM)) {
            return;
        }

        if (from.equals(config.getServerName())) {
            return;
        }

        var component = new TextComponent();

        if (sender.isBlank()) { // Todo: qyl27: Do not forward player death?
            if (!config.showSystemMessage()) {
                return;
            }
        } else {
            var senderComponent = new TextComponent("[" + sender + "] ");
            senderComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("时间：" + time)));
            senderComponent.setColor(ChatColor.GREEN);
            component.addExtra(senderComponent);
        }

        var trimLength = config.getTrimLength();
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

        player.getServer().spigot().broadcast(component);
    }
}
