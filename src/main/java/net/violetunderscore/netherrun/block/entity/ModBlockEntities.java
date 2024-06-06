package net.violetunderscore.netherrun.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.block.ModBlocks;


public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NetherRun.MODID);

    public static final RegistryObject<BlockEntityType<BlockOfGoUpBlockEntity>> BLOCK_OF_GO_UP_BE =
            BLOCK_ENTITIES.register("block_of_go_up_be", () ->
                    BlockEntityType.Builder.of(BlockOfGoUpBlockEntity::new,
                            ModBlocks.BLOCK_OF_GO_UP.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
