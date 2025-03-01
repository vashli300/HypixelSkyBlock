package net.swofty.gui.inventory.inventories.shop;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointDouble;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.SkyBlockShopGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.updater.NonPlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public final class TradingOptionsGUI extends SkyBlockInventoryGUI
{
      private final SkyBlockShopGUI.ShopItem item;
      private final SkyBlockShopGUI retPointer;
      private final double stackPrice;

      public TradingOptionsGUI(SkyBlockShopGUI.ShopItem item, SkyBlockShopGUI retPoint, double stackPrice) {
            super("Shop Trading Options", InventoryType.CHEST_6_ROW);
            this.item = item;
            this.retPointer = retPoint;
            this.stackPrice = stackPrice;
      }

      @Override
      public void onOpen(InventoryGUIOpenEvent e) {
            fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
            set(createTradeItem(item, 20, 1, getPlayer(), stackPrice));
            set(createTradeItem(item, 21, 5, getPlayer(), stackPrice));
            set(createTradeItem(item, 22, 10, getPlayer(), stackPrice));
            set(createTradeItem(item, 23, 32, getPlayer(), stackPrice));
            set(createTradeItem(item, 24, 64, getPlayer(), stackPrice));

            set(GUIClickableItem.getGoBackItem(49, retPointer));

            updateItemStacks(e.inventory(), getPlayer());
      }

      private GUIClickableItem createTradeItem(SkyBlockShopGUI.ShopItem item, int slot, int amount, SkyBlockPlayer player, double stackprice) {
            stackprice = stackprice * amount;
            SkyBlockItem sbItem = item.item();
            ItemStack.Builder itemStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();
            List<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
            lore.add("");
            lore.add("§7Cost");
            lore.add("§6 " + StringUtility.commaify(stackprice) + " Coin" + (stackprice != 1 ? "s" : ""));
            lore.add("");
            lore.add("§7Stock");
            lore.add("§6 " + player.getShoppingData().getStock(item.item()) + " §7remaining");
            lore.add("");
            lore.add("§eClick to purchase!");

            double finalStackprice = stackprice;
            return new GUIClickableItem()
            {
                  @Override
                  public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        if (!player.getShoppingData().canPurchase(item.item(), amount)) {
                              player.sendMessage("§cYou have reached the maximum amount of items you can buy!");
                              return;
                        }

                        double purse = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
                        if (finalStackprice > purse) {
                              player.sendMessage("§cYou don't have enough coins!");
                              return;
                        }
                        ItemStack.Builder cleanStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();
                        cleanStack.amount(amount);
                        player.getInventory().addItemStack(cleanStack.build());
                        player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
                        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(purse - finalStackprice);
                        player.getShoppingData().documentPurchase(item.item(), amount);
                        updateThis(player);
                  }

                  @Override
                  public int getSlot() {
                        return slot;
                  }

                  @Override
                  public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        String displayName = StringUtility.getTextFromComponent(itemStack.build().getDisplayName().append(Component.text(" §8x" + amount)));
                        return ItemStackCreator.getStack(displayName, itemStack.build().material(), 0, amount, lore);
                  }
            };
      }

      private void updateThis(SkyBlockPlayer player) {
            this.open(player);
      }

      @Override
      public boolean allowHotkeying() {
            return false;
      }

      @Override
      public void onClose(InventoryCloseEvent e, CloseReason reason) {

      }

      @Override
      public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

      }

      @Override
      public void onBottomClick(InventoryPreClickEvent e) {

      }
}
