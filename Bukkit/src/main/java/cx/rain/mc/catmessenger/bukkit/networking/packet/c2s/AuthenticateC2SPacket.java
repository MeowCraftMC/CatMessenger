package cx.rain.mc.catmessenger.bukkit.networking.packet.c2s;

import cx.rain.mc.catmessenger.bukkit.utility.CborWriter;
import cx.rain.mc.catmessenger.common.Constants;

public class AuthenticateC2SPacket extends C2SPacket {
    private final String serverName;
    private final String connectorSecret;

    public AuthenticateC2SPacket(String serverName, String connectorSecret) {
        this.serverName = serverName;
        this.connectorSecret = connectorSecret;
    }

    @Override
    protected void write(CborWriter writer) {
        writer.writeString(Constants.PACKET_AUTHENTICATE);
        writer.writeString(connectorSecret);
        writer.writeString(serverName);
    }
}
