package net.violetunderscore.netherrun.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.violetunderscore.netherrun.NetherRun;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NetherRun.MODID);

    public static final RegistryObject<Item> NETHERRUN_PICKAXE = ITEMS.register("netherrun_pickaxe",
            () -> new PickaxeItem(Tiers.NETHERITE, -4, -2.8f, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> NETHERRUN_TOTEM = ITEMS.register("netherrun_totem",
            () -> new Item(new Item.Properties().fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
