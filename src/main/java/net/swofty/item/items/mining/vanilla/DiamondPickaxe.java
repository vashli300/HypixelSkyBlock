package net.swofty.item.items.mining.vanilla;

import net.swofty.item.ReforgeType;
import net.swofty.item.impl.*;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.ItemGroups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiamondPickaxe implements CustomSkyBlockItem, MiningTool, ExtraRarityDisplay, Enchantable, Reforgable {
      @Override
      public ItemStatistics getStatistics() {
            return ItemStatistics.builder()
                    .with(ItemStatistic.MINING_SPEED, 230)
                    .with(ItemStatistic.DAMAGE, 30)
                    .build();
      }

      @Override
      public int getBreakingPower() {
            return 4;
      }

      @Override
      public String getExtraRarityDisplay() {
            return " PICKAXE";
      }

      @Override
      public boolean showEnchantLores() {
            return true;
      }

      @Override
      public List<ItemGroups> getItemGroups() {
            return List.of(ItemGroups.PICKAXE, ItemGroups.TOOLS);
      }

      @Override
      public ReforgeType getReforgeType() {
            return ReforgeType.PICKAXES;
      }
}
