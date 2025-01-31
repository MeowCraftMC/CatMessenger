package cx.rain.mc.catmessenger.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cx.rain.mc.catmessenger.mod.CatMessengerMod;
import cx.rain.mc.catmessenger.mod.config.ModConfig;
import dev.architectury.event.events.common.LifecycleEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;

public class CatMessengerFabric implements ModInitializer {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onInitialize() {
        var config = new ModConfig();
        var configPath = FabricLoader.getInstance().getConfigDir().resolve("catmessenger.json");

        if (!Files.exists(configPath)) {
            try {
                Files.writeString(configPath, GSON.toJson(config));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            config = GSON.fromJson(Files.readString(configPath), ModConfig.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        var mod = new CatMessengerMod(config);

        LifecycleEvent.SERVER_STARTED.register(mod::start);
        LifecycleEvent.SERVER_STOPPING.register(mod::stop);
    }
}
