package net.violetunderscore.netherrun.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.entity.custom.NetherRunBoatEntity;
import net.violetunderscore.netherrun.entity.custom.NetherRunChestBoatEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NetherRun.MODID);

    public static final RegistryObject<EntityType<NetherRunBoatEntity>> NETHERRUN_BOAT =
            ENTITY_TYPES.register("netherrun_boat", () -> EntityType.Builder.<NetherRunBoatEntity>of(NetherRunBoatEntity::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("netherrun_boat"));


    public static final RegistryObject<EntityType<NetherRunChestBoatEntity>> NETHERRUN_CHEST_BOAT =
            ENTITY_TYPES.register("netherrun_chest_boat", () -> EntityType.Builder.<NetherRunChestBoatEntity>of(NetherRunChestBoatEntity::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("netherrun_chest_boat"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
