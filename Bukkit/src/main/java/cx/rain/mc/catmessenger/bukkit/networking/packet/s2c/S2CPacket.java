package cx.rain.mc.catmessenger.bukkit.networking.packet.s2c;

import cx.rain.mc.catmessenger.bukkit.MessengerBukkit;
import cx.rain.mc.catmessenger.bukkit.utility.CborReader;

public abstract class S2CPacket {
    public abstract void handle(MessengerBukkit plugin, CborReader reader);
}
