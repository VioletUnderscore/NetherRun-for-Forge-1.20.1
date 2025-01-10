package net.violetunderscore.netherrun;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.block.entity.ModBlockEntities;
import net.violetunderscore.netherrun.commands.NetherRunCommands;
import net.violetunderscore.netherrun.entity.ModEntities;
import net.violetunderscore.netherrun.entity.client.NetherRunBoatRenderer;
import net.violetunderscore.netherrun.item.ModCreativeModeTabs;
import net.violetunderscore.netherrun.item.ModItems;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.particle.ModParticles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(NetherRun.MODID)
public class NetherRun
{
    //My Variables
    // Define mod id in a common place for everything to reference
    public static final String MODID = "netherrun";
    public static final String MODVER = "m8d10-1";
    public static final Boolean SHOWMODVER = true;
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogManager.getLogger();
    public NetherRun()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModEntities.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModParticles.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);



        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        NetworkHandler.register();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        NetherRunCommands.register(event.getDispatcher());
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.NETHERRUN_BOAT.get(), pContext -> new NetherRunBoatRenderer(pContext, false));
            EntityRenderers.register(ModEntities.NETHERRUN_CHEST_BOAT.get(), pContext -> new NetherRunBoatRenderer(pContext, true));
        }
    }
}
