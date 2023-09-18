package cx.rain.mc.catmessenger.bungee.hadler;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cx.rain.mc.catmessenger.bungee.utility.MessageSendHelper;
import cx.rain.mc.catmessenger.common.Constants;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageHandler implements Listener {
    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase(Constants.CHANNEL_ID)) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        var platform = input.readUTF();
        var from = input.readUTF();
        var sender = input.readUTF();
        var time = input.readUTF();
        var content = input.readUTF();

        if (platform.equalsIgnoreCase(Constants.CHANNEL_PLATFORM_MINECRAFT_BUKKIT)) {
            MessageSendHelper.broadcastMessage(platform, from, sender, time, content);
        }
    }
}
