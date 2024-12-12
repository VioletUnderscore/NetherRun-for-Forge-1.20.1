package net.violetunderscore.netherrun.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScores;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresProvider;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKits;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKitsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod.EventBusSubscriber(modid = NetherRun.MODID/*, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER*/)
public class ModEventBusEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerKits.class);
        event.register(NetherRunScores.class);
    }



    //PLAYER CAPABILITY
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerKitsProvider.PLAYER_KITS).isPresent()) {
                event.addCapability(new ResourceLocation(NetherRun.MODID, "properties"), new PlayerKitsProvider());
            }
        }
    }





    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }





    //NETHERRUN CAPABILITY
    @SubscribeEvent
    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject().dimension() == Level.OVERWORLD) {
            if (!event.getObject().getCapability(NetherRunScoresProvider.NETHERRUN_SCORES).isPresent()) {
                LOGGER.info("Attaching NetherRunScores capability to Overworld");
                event.addCapability(new ResourceLocation(NetherRun.MODID, "scores"), new NetherRunScoresProvider());
            }
        }
    }





    // MISCELLANEOUS
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        event.player.clearFire();
        if(event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                event.player.getInventory().offhand.set(0, new ItemStack(kit.getKitItem(0), kit.getKitItem(0).getMaxStackSize()));
                for (int v = 0; v <= 8; v++) {
                    //event.player.getInventory().setItem(v, new ItemStack(kit.getKitItem(v + 1), kit.getKitItem(v + 1).getMaxStackSize()));
                }
            });
        }

    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.level.dimension() == Level.OVERWORLD) {
            event.level.getCapability(NetherRunScoresProvider.NETHERRUN_SCORES).ifPresent(scores -> {
                scores.addScore(0, 1);
            });
        }
    }

}
