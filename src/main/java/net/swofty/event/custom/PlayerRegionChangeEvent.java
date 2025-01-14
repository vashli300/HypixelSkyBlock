package net.swofty.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.region.RegionType;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerRegionChangeEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    @Getter
    private final RegionType from;
    @Getter
    private final RegionType to;

    public PlayerRegionChangeEvent(SkyBlockPlayer player, RegionType from, RegionType to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    @Override
    public @NotNull SkyBlockPlayer getPlayer() {
        return player;
    }
}
