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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod.EventBusSubscriber(modid = NetherRun.MODID, /*bus = Mod.EventBusSubscriber.Bus.MOD,*/ value = Dist.CLIENT)
public class ModEventBusEventsCLIENT {

    private static final Logger LOGGER = LogManager.getLogger();
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.GO_UP_PARTICLES.get(),
                GoUpParticles.Provider::new);
        Minecraft.getInstance().particleEngine.register(ModParticles.GO_UP_PLACED_PARTICLES.get(),
                GoUpPlacedParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.NETHERRUN_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.NETHERRUN_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("nr_scoreboard", NetherRunScoresDisplay.HUD_NR_SCORE_DISPLAY);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        while (NetherRunKeybinds.NETHERRUN_TELEPORT.consumeClick()) {

        }
    }

}
