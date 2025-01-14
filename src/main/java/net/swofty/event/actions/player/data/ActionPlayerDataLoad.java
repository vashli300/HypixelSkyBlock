package net.swofty.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointInventory;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.item.updater.PlayerItemOrigin;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockInventory;
import net.swofty.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.UUID;

@EventParameters(description = "Load player data on join",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionPlayerDataLoad extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;

        // Ensure we use player here
        final Player player = playerLoginEvent.getPlayer();
        UUID uuid = player.getUuid();

        UserDatabase userDatabase = new UserDatabase(uuid.toString());
        DataHandler handler;

        if (userDatabase.exists()) {
            Document document = userDatabase.getDocument();
            handler = DataHandler.fromDocument(document);
            DataHandler.userCache.put(uuid, handler);
        } else {
            handler = DataHandler.initUserWithDefaultData(uuid);
            DataHandler.userCache.put(uuid, handler);
        }

        handler.runOnLoad();
    }
}
