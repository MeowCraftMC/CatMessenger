package cx.rain.mc.catmessenger.server;

import cx.rain.mc.catmessenger.server.processor.MessageProcessor;
import net.afyer.afybroker.server.plugin.Plugin;

public class CatMessengerServer extends Plugin {
    @Override
    public void onEnable() {
        getServer().registerUserProcessor(new MessageProcessor());
    }
}
