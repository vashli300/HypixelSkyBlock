package net.swofty.packet;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.swofty.SkyBlock;
import net.swofty.user.SkyBlockPlayer;

import java.util.ArrayList;

public abstract class SkyBlockPacketClientListener {
    private static ArrayList<SkyBlockPacketClientListener> cachedEvents = new ArrayList<>();
    public abstract Class<? extends ClientPacket> getPacket();

    public abstract void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player);

    public void cacheListener() {
        cachedEvents.add(this);
    }

    public static void register(GlobalEventHandler eventHandler) {
        EventNode<Event> eventNode = EventNode.all("packet-handler-client");

        eventNode.addListener(PlayerPacketEvent.class, rawEvent -> {
            cachedEvents.forEach((packetEvent) -> {
                Class<?> packetType = packetEvent.getPacket();
                if (packetType.isInstance(rawEvent.getPacket())) {
                    if (SkyBlock.getLoadedPlayers().contains((SkyBlockPlayer) rawEvent.getPlayer()))
                        packetEvent.run(rawEvent, rawEvent.getPacket(), (SkyBlockPlayer) rawEvent.getPlayer());
                }
            });
        });
        eventHandler.addChild(eventNode);
    }
}
