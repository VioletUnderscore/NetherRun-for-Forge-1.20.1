package net.violetunderscore.netherrun.event;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.item.ModItems;
import net.violetunderscore.netherrun.network.GoUpParticlePacket;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.particle.ModParticles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

;import java.util.Random;

@Mod.EventBusSubscriber(modid = NetherRun.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {

    private static final Logger LOGGER = LogManager.getLogger();
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        //LOGGER.info("onLivingAttack has been called with MsgID: '" + event.getSource().getMsgId() + "'");
        if (event.getSource().getMsgId().equals("lava") || event.getSource().getMsgId().equals("fall")) {
            //LOGGER.info("Damage is either 'lava' or 'fall'");
            if (event.getEntity().isInLava()) {
                //LOGGER.info("You are in lava");
                if (event.getEntity().getMainHandItem().getItem() == ModItems.NETHERRUN_TOTEM.get() && event.getEntity().getMainHandItem().getTag().getBoolean("netherrun.ready")) {
                    //LOGGER.info("Totem is ready, in your main hand, and your damage has been cancelled");
                    event.setCanceled(true);
                }
                else if (event.getEntity().getOffhandItem().getItem() == ModItems.NETHERRUN_TOTEM.get() && event.getEntity().getOffhandItem().getTag().getBoolean("netherrun.ready")) {
                    //LOGGER.info("Totem is ready, in your off hand, and your damage has been cancelled");
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
            //LOGGER.info("NETHERRUN: onEntityPlace is being called");
            if (event.getPlacedBlock().is(ModBlocks.GO_UP.get())) {
                //LOGGER.info("NETHERRUN: If statement is working");
                for (int i = 0; i < 15; i++) {
                    //LOGGER.info("NETHERRUN: For loop is working");
                    Random randomPosX = new Random();
                    Random randomPosY = new Random();
                    Random randomPosZ = new Random();

                    NetworkHandler.sendToNear(new GoUpParticlePacket(
                            randomPosX.nextDouble() + event.getPos().getX(),
                            randomPosY.nextDouble() / 2 + event.getPos().getY(),
                            randomPosZ.nextDouble() + event.getPos().getZ(),
                            ModParticles.GO_UP_PLACED_PARTICLES.get()

                    ), event.getLevel().getServer(), 32);

                    /*event.getEntity().level().addParticle(ModParticles.GO_UP_PLACED_PARTICLES.get(),
                            event.getPos().getX() + ((float) (randomPosX.nextInt(60) + 20) / 100),
                            event.getPos().getY() + ((float) (randomPosY.nextInt(60) + 20) / 100),
                            event.getPos().getZ() + ((float) (randomPosZ.nextInt(60) + 20) / 100),
                            0, 1, 0);*/
                }
            }

    }
}