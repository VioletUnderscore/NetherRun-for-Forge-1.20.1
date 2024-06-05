package net.violetunderscore.netherrun.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.block.ModBlocks;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NetherRun.MODID);

    public static final RegistryObject<CreativeModeTab> NETHERRUN_TAB = CREATIVE_MODE_TABS.register("netherrun_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.NETHERRUN_PICKAXE.get()))
                    .title(Component.translatable("creativetab.netherrun_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.BLOCK_OF_GO_UP.get());
                        pOutput.accept(ModBlocks.GO_UP.get());
                        pOutput.accept(ModBlocks.BLOCK_OF_GO_DOWN.get());

                        pOutput.accept(ModItems.NETHERRUN_PICKAXE.get());
                        pOutput.accept(ModItems.NETHERRUN_TOTEM.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
