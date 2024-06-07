package net.violetunderscore.netherrun.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.block.custom.BlockOfGoUp;
import net.violetunderscore.netherrun.block.custom.GoUpBlock;
import net.violetunderscore.netherrun.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, NetherRun.MODID);

    public static final RegistryObject<Block> BLOCK_OF_GO_UP = registerBlock("block_of_go_up",
            () -> new BlockOfGoUp(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).pushReaction(PushReaction.BLOCK)));
    public static final RegistryObject<Block> GO_UP = registerBlock("go_up",
            () -> new GoUpBlock(BlockBehaviour.Properties.of().replaceable().noCollission().noLootTable()));

    public static final RegistryObject<Block> BLOCK_OF_GO_DOWN = registerBlock("block_of_go_down",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).pushReaction(PushReaction.BLOCK)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().fireResistant()));
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
