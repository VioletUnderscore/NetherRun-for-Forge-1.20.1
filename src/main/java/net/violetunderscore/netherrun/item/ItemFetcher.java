package net.violetunderscore.netherrun.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemFetcher {
    public static Item getItem(String itemName) {
        ResourceLocation resourceLocation = new ResourceLocation(itemName);

        return BuiltInRegistries.ITEM.get(resourceLocation);
    }

    public static String ungetItem(Item item) {
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(item);

        return resourceLocation.toString();
    }
}
