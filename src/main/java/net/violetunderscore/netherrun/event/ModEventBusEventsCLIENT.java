package net.violetunderscore.netherrun.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.violetunderscore.netherrun.NetherRun;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.violetunderscore.netherrun.client.NetherRunScoresDisplay;
import net.violetunderscore.netherrun.client.keybinds.NetherRunKeybinds;
import net.violetunderscore.netherrun.entity.client.ModModelLayers;
import net.violetunderscore.netherrun.particle.ModParticles;
import net.violetunderscore.netherrun.particle.custom.GoUpParticles;
import net.violetunderscore.netherrun.particle.custom.GoUpPlacedParticles;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKitsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod.EventBusSubscriber(modid = NetherRun.MODID, /*bus = Mod.EventBusSubscriber.Bus.MOD,*/ value = Dist.CLIENT)
public class ModEventBusEventsCLIENT {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                if (NetherRunKeybinds.NETHERRUN_TELEPORT.isDown()) {
                    minecraft.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                        kit.setKeyDown(true);
                    });
                }
                else {
                    minecraft.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                        kit.setKeyDown(false);
                    });
                }
            }
        }
    }

}
