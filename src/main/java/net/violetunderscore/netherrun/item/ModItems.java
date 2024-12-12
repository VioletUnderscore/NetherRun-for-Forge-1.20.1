package net.violetunderscore.netherrun.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.entity.custom.NetherRunBoatEntity;
import net.violetunderscore.netherrun.item.custom.NetherRunBoatItem;
import net.violetunderscore.netherrun.item.custom.NetherrunPickaxeItem;
import net.violetunderscore.netherrun.item.custom.NetherrunTotemItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NetherRun.MODID);

    public static final RegistryObject<Item> NETHERRUN_PICKAXE = ITEMS.register("netherrun_pickaxe",
            () -> new NetherrunPickaxeItem(Tiers.NETHERITE, -4, -2.8f, new Item.Properties().fireResistant().durability(69420)));
    public static final RegistryObject<Item> NETHERRUN_TOTEM = ITEMS.register("netherrun_totem",
            () -> new NetherrunTotemItem(new Item.Properties().fireResistant().stacksTo(1)));



    public static final RegistryObject<Item> NETHERRUN_BOAT = ITEMS.register("netherrun_boat",
            () -> new NetherRunBoatItem(false, NetherRunBoatEntity.Type.NETHERRUN, new Item.Properties().fireResistant().stacksTo(1)));
    public static final RegistryObject<Item> NETHERRUN_CHEST_BOAT = ITEMS.register("netherrun_chest_boat",
            () -> new NetherRunBoatItem(true, NetherRunBoatEntity.Type.NETHERRUN, new Item.Properties().fireResistant().stacksTo(1)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
