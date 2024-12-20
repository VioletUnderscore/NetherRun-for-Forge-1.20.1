package net.violetunderscore.netherrun.event;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.item.ModItems;
import net.violetunderscore.netherrun.network.GoUpParticlePacket;
import net.violetunderscore.netherrun.network.ItemCooldownPacket;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.particle.ModParticles;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresData;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresDataManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

;import java.util.Objects;
import java.util.Random;

import static net.violetunderscore.netherrun.block.custom.GoUpBlock.*;



@Mod.EventBusSubscriber(modid = NetherRun.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {

    private static final Logger LOGGER = LogManager.getLogger();

//    private static LazyOptional<INetherRunData> getGlobalData(Level level) {
//        if (level.getServer() != null) {
//            ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
//            if (overworld != null) {
//                return overworld.getCapability(NetherRunCapabilityProvider.NETHER_RUN_CAPABILITY);
//            }
//        }
//        return LazyOptional.empty();
//    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        //LOGGER.info("onLivingAttack has been called with MsgID: '" + event.getSource().getMsgId() + "'");
        boolean totemNegatedDamage = false;
        if (event.getSource().getMsgId().equals("lava") || event.getSource().getMsgId().equals("fall")) {
            //LOGGER.info("Damage is either 'lava' or 'fall'");
            if (event.getEntity().isInLava()) {
                //LOGGER.info("You are in lava");
                if (event.getEntity().getMainHandItem().getItem() == ModItems.NETHERRUN_TOTEM.get() && event.getEntity().getMainHandItem().getTag().getBoolean("netherrun.ready")) {
                    //LOGGER.info("Totem is ready, in your main hand, and your damage has been cancelled");
                    event.setCanceled(true);
                    totemNegatedDamage = true;
                }
                else if (event.getEntity().getOffhandItem().getItem() == ModItems.NETHERRUN_TOTEM.get() && event.getEntity().getOffhandItem().getTag().getBoolean("netherrun.ready")) {
                    //LOGGER.info("Totem is ready, in your off hand, and your damage has been cancelled");
                    event.setCanceled(true);
                    totemNegatedDamage = true;
                }
            }
        }
        if (event.getEntity() instanceof Player && !totemNegatedDamage && !event.getEntity().isInvulnerable()) {
            try {
                ServerLevel overworld = event.getEntity().getServer().getLevel(Level.OVERWORLD);
                NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);
                if (scoresData.isRoundActive()) {
                    if ((Objects.equals(scoresData.getPlayer1Name(), event.getEntity().getName().getString())
                            && scoresData.getWhosTurn() == 1)
                            ||
                            (Objects.equals(scoresData.getPlayer2Name(), event.getEntity().getName().getString())
                                    && scoresData.getWhosTurn() == 2)
                    ) {
                        if (event.getEntity().getHealth() - event.getAmount() <= 0) {
                            specAll(event.getEntity().getServer());
                            scoresData.setRoundJustEnded(true);
                            scoresData.setRoundActive(false);
                            event.setCanceled(true);
                            event.getEntity().setGlowingTag(false);
                        }
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player) {
            try {
                ServerLevel overworld = event.getEntity().getServer().getLevel(Level.OVERWORLD);
                NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);
                if (scoresData.isRoundActive()) {
                    if ((Objects.equals(scoresData.getPlayer1Name(), event.getEntity().getName().getString())
                            && scoresData.getWhosTurn() == 2)
                            ||
                            (Objects.equals(scoresData.getPlayer2Name(), event.getEntity().getName().getString())
                                    && scoresData.getWhosTurn() == 1)
                    ) {
                        event.setAmount(0);
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof  Player) {
            Player pPlayer = (Player) event.getEntity();
            //LOGGER.info("NETHERRUN: onEntityPlace is being called");
            if (event.getPlacedBlock().is(ModBlocks.GO_UP.get())) {
                event.getPlacedBlock().setValue(PLAYER_PLACED, true);
                event.getPlacedBlock().setValue(LIFETIME, 12);
                if (!pPlayer.isCreative()) {
                    pPlayer.getCooldowns().addCooldown(ModBlocks.GO_UP.get().asItem(), 15);
                }
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
                }
            } else if (event.getPlacedBlock().is(ModBlocks.BLOCK_OF_GO_UP.get()) && !pPlayer.isCreative()) {
                pPlayer.getCooldowns().addCooldown(ModBlocks.BLOCK_OF_GO_UP.get().asItem(), 300);
            }
        }
    }

//    @SubscribeEvent
//    public static void onDeath (LivingDeathEvent event) {
//
//    }

    @SubscribeEvent
    public static void itemTossEvent(ItemTossEvent event) {
        try {
            ServerLevel overworld = event.getEntity().getServer().getLevel(Level.OVERWORLD);
            NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);
            if (scoresData.isGameActive()) {
                ItemStack itemStack = event.getEntity().getItem();
                event.setCanceled(true);
                event.getPlayer().getInventory().setItem(event.getPlayer().getInventory().selected, itemStack);
            }
        } catch (NullPointerException e) {

        }
    }

    private static void specAll(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // Set each player to Spectator Mode
            player.setGameMode(GameType.SPECTATOR);
        }
    }
}