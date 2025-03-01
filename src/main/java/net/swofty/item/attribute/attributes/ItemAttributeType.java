package net.swofty.item.attribute.attributes;

import net.swofty.item.attribute.ItemAttribute;

public class ItemAttributeType extends ItemAttribute<String> {

    @Override
    public String getKey() {
        return "item_type";
    }

    @Override
    public String getDefaultValue() {
        return "N/A";
    }

    @Override
    public String loadFromString(String string) {
        return string;
    }

    @Override
    public String saveIntoString() {
        return this.value;
    }
}
