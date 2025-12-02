package cx.rain.mc.catmessenger.paper.handler;

import cx.rain.mc.catmessenger.api.utilities.ComponentSerializer;
import cx.rain.mc.catmessenger.paper.utility.BukkitMessageHelper;
import cx.rain.mc.catmessenger.paper.utility.MessengerHelper;
import io.papermc.paper.event.entity.TameableDeathMessageEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityEventHandler implements Listener {
    @EventHandler
    public void onTameableDeath(TameableDeathMessageEvent event) {    // Paper, you saved my whole night!
        if (event.isCancelled()) {
            return;
        }

        var entity = event.getEntity();
        var owner = entity.getOwner();
        if (!entity.isTamed() || owner == null) {
            return;
        }

        var player = BukkitMessageHelper.createPlayer(owner.getName(), owner.getUniqueId());
        var message = ComponentSerializer.toPlain(event.deathMessage());
        var component = BukkitMessageHelper.petDeath(player, Component.text(message));
        MessengerHelper.send(component);
    }

    @EventHandler
    public void onNamedEntityDeath(EntityDeathEvent event) {
        if (event.isCancelled()) {
            return;
        }

        var entity = event.getEntity();
        if (entity instanceof Player) {
            return;
        }

        if (entity instanceof Tameable) {
            return;
        }

        var name = entity.customName();
        if (name == null) {
            return;
        }

        var message = BukkitMessageHelper.namedDeath(name, BukkitMessageHelper.createEntityName(entity), entity.getLocation());
        MessengerHelper.send(message);
    }
}
