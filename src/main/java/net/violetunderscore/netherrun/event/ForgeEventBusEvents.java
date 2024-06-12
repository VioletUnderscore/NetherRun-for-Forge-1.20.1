package net.violetunderscore.netherrun.event;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.item.ModItems;

;

@Mod.EventBusSubscriber(modid = NetherRun.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getMsgId().equals("lava") || event.getSource().getMsgId().equals("fall")) {
            if (event.getEntity().isInLava()) {
                if (event.getEntity().getMainHandItem().getItem() == ModItems.NETHERRUN_TOTEM.get() && event.getEntity().getMainHandItem().getTag().getBoolean("netherrun.totem_ready")) {
                    event.setCanceled(true);
                }
            }
            else if (event.getEntity().getOffhandItem().getItem() == ModItems.NETHERRUN_TOTEM.get() && event.getEntity().getOffhandItem().getTag().getBoolean("netherrun.totem_ready")) {
                event.setCanceled(true);
            }
        }
    }
}