package net.swofty.item.attribute.attributes;

import net.swofty.item.attribute.ItemAttribute;

public class ItemAttributeRecombobulated extends ItemAttribute<Boolean> {

    @Override
    public String getKey() {
        return "recombobulated";
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }

    @Override
    public Boolean loadFromString(String string) {
        return Boolean.valueOf(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.toString();
    }
}
