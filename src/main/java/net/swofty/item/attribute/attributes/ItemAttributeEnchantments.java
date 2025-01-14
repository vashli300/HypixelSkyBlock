package net.swofty.item.attribute.attributes;

import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.item.Rarity;
import net.swofty.item.attribute.ItemAttribute;

import java.util.ArrayList;
import java.util.List;

public class ItemAttributeEnchantments extends ItemAttribute<SkyBlockEnchantment.ItemEnchantments> {

    @Override
    public String getKey() {
        return "enchantments";
    }

    @Override
    public SkyBlockEnchantment.ItemEnchantments getDefaultValue() {
        return new SkyBlockEnchantment.ItemEnchantments(new ArrayList<>());
    }

    @Override
    public SkyBlockEnchantment.ItemEnchantments loadFromString(String string) {
        List<SkyBlockEnchantment> enchantments = new ArrayList<>();

        if (string.isEmpty()) {
            return new SkyBlockEnchantment.ItemEnchantments(enchantments);
        }

        String[] split = string.split(",");
        for (String enchantment : split) {
            enchantments.add(SkyBlockEnchantment.deserialize(enchantment));
        }

        return new SkyBlockEnchantment.ItemEnchantments(enchantments);
    }

    @Override
    public String saveIntoString() {
        List<String> serializedEnchantments = new ArrayList<>();

        this.value.enchantments().forEach(enchantment -> {
            serializedEnchantments.add(enchantment.serialize());
        });

        return String.join(",", serializedEnchantments);
    }
}
