package net.violetunderscore.netherrun.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.violetunderscore.netherrun.NetherRun;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.violetunderscore.netherrun.client.NetherRunScoresDisplay;
import net.violetunderscore.netherrun.particle.ModParticles;
import net.violetunderscore.netherrun.particle.custom.GoUpParticles;
import net.violetunderscore.netherrun.particle.custom.GoUpPlacedParticles;


@Mod.EventBusSubscriber(modid = NetherRun.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.GO_UP_PARTICLES.get(),
                GoUpParticles.Provider::new);
        Minecraft.getInstance().particleEngine.register(ModParticles.GO_UP_PLACED_PARTICLES.get(),
                GoUpPlacedParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("nr_scoreboard", NetherRunScoresDisplay.HUD_NR_SCORE_DISPLAY);
    }
}
