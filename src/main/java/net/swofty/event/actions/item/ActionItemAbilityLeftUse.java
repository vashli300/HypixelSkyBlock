package net.swofty.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.CustomSkyBlockAbility;
import net.swofty.user.PlayerAbilityHandler;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.StatisticDisplayReplacement;

@EventParameters(description = "Handles item ability use for left clicks",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
public class ActionItemAbilityLeftUse extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerHandAnimationEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerHandAnimationEvent playerUseItemEvent = (PlayerHandAnimationEvent) event;
        ItemStack itemStack = playerUseItemEvent.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) playerUseItemEvent.getPlayer();

        if (item.clazz != null && item.clazz.newInstance() instanceof CustomSkyBlockAbility ability) {
            if (ability.getAbilityActivation() == CustomSkyBlockAbility.AbilityActivation.LEFT_CLICK) {
                if (ability.getManaCost() > player.getMana()) {
                    player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                            .display("§c§lNOT ENOUGH MANA")
                            .ticksToLast(20 * 2)
                            .build(), StatisticDisplayReplacement.DisplayType.MANA);
                    return;
                }

                PlayerAbilityHandler abilityHandler = player.getAbilityHandler();

                if (!abilityHandler.canUseAbility(item, ability.getAbilityCooldownTicks())) {
                    player.sendMessage("§cThis ability is on cooldown for " +
                            Math.round((float) abilityHandler.getRemainingCooldown(item, ability.getAbilityCooldownTicks()) / 1000) + "s.");
                    return;
                }

                player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                        .display("§b-" + ability.getManaCost() + " (§6" + ability.getAbilityName() + "§b)")
                        .ticksToLast(20 * 2)
                        .build(), StatisticDisplayReplacement.DisplayType.DEFENSE);
                abilityHandler.startAbilityCooldown(item);
                player.setMana(player.getMana() - ability.getManaCost());

                ability.onAbilityUse(player, item);
            }
        }
    }
}
