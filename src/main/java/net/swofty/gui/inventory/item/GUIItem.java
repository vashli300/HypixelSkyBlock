package net.swofty.gui.inventory.item;

import net.minestom.server.item.ItemStack;
import net.swofty.user.SkyBlockPlayer;

public interface GUIItem
{
    int getSlot();

    ItemStack.Builder getItem(SkyBlockPlayer player);

    /**
     * If the player can pick up the item from the inventory or not
     *
     * @return a boolean
     */
    default boolean canPickup() {
        return false;
    }
}