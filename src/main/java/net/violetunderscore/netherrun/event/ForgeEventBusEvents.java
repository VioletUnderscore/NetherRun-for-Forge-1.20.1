package net.violetunderscore.netherrun.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.item.ModItems;
import net.violetunderscore.netherrun.network.packets.GoUpParticlePacket;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.particle.ModParticles;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresData;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresDataManager;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKitsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

;import java.util.Objects;
import java.util.Random;

import static net.violetunderscore.netherrun.block.custom.GoUpBlock.*;



@Mod.EventBusSubscriber(modid = NetherRun.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation CROSSHAIR = new ResourceLocation("minecraft", "crosshair");

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
        if (!event.getEntity().level().isClientSide) {
            boolean totemNegatedDamage = false;
            if (event.getSource().getMsgId().equals("lava") || event.getSource().getMsgId().equals("fall") && event.getEntity().hurtTime == 0) {
                //LOGGER.info("Damage is either 'lava' or 'fall'");
                if (event.getEntity().isInLava()) {
                    //LOGGER.info("You are in lava");
                    if (event.getEntity().getMainHandItem().getItem() == ModItems.NETHERRUN_TOTEM.get() && event.getEntity().getMainHandItem().getTag().getBoolean("netherrun.ready")) {
                        //LOGGER.info("Totem is ready, in your main hand, and your damage has been cancelled");
                        event.setCanceled(true);
                        totemNegatedDamage = true;
                    } else if (event.getEntity().getOffhandItem().getItem() == ModItems.NETHERRUN_TOTEM.get() && event.getEntity().getOffhandItem().getTag().getBoolean("netherrun.ready")) {
                        //LOGGER.info("Totem is ready, in your off hand, and your damage has been cancelled");
                        event.setCanceled(true);
                        totemNegatedDamage = true;
                    }
                }
            }
            if (event.getEntity() instanceof Player && !totemNegatedDamage) {
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
                            if (event.getEntity().hurtTime == 0) {
                                if (event.getEntity().getHealth() - getTrueDamage(event) <= 0) {
                                    specAll(event.getEntity().getServer());
                                    scoresData.setRoundJustEnded(true);
                                    scoresData.setRoundActive(false);
                                    scoresData.setSpawnTimerR(0);
                                    scoresData.setSpawnTimerH(0);
                                    event.setCanceled(true);
                                    event.getEntity().setGlowingTag(false);
                                }
                            }
                            else {
                                if (event.getEntity().getHealth() + event.getEntity().getPersistentData().getFloat("netherrun:hurt_amount") - getTrueDamage(event) <= 0) {
                                    specAll(event.getEntity().getServer());
                                    scoresData.setRoundJustEnded(true);
                                    scoresData.setRoundActive(false);
                                    scoresData.setSpawnTimerR(0);
                                    scoresData.setSpawnTimerH(0);
                                    event.setCanceled(true);
                                    event.getEntity().setGlowingTag(false);
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                }
            }
            if (event.getEntity().hurtTime == 0 || getTrueDamage(event) > event.getEntity().getPersistentData().getFloat("netherrun:hurt_amount")) {
                event.getEntity().getPersistentData().putFloat("netherrun:hurt_amount", getTrueDamage(event));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
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
        event.getEntity().getPersistentData().putFloat("LastDamageAmount", getTrueDamage(event));
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
                    pPlayer.getCooldowns().addCooldown(ModBlocks.GO_UP.get().asItem(), 25);
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
                pPlayer.getCooldowns().addCooldown(ModBlocks.BLOCK_OF_GO_UP.get().asItem(), 20 * 20);
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

    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        if (event.getEntityMounting() instanceof  Player && event.getEntityBeingMounted() instanceof Boat) {
            Player player = (Player) event.getEntityMounting();
            if (event.isMounting()) {
                player.getCooldowns().addCooldown(ModItems.NETHERRUN_BOAT.get(), 50);
            }
            else {
                player.getCooldowns().removeCooldown(ModItems.NETHERRUN_BOAT.get());
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHud(RenderGuiOverlayEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player.getCooldowns().isOnCooldown(player.getInventory().getItem(player.getInventory().selected).getItem()) && event.getOverlay().id().equals(CROSSHAIR)) {
            event.setCanceled(true);
        }
    }

    private static void specAll(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // Set each player to Spectator Mode
            player.setGameMode(GameType.SPECTATOR);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        LOGGER.info("onPlayerClone ran");
        if (!event.isWasDeath()) return;

        event.getOriginal().getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(oldKit -> {
            event.getEntity().getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(newKit -> {
                newKit.copyFrom(oldKit);

                LOGGER.info("newKit.copyFrom ran");
            });
        });
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(
                    new ResourceLocation(NetherRun.MODID, "player_kits"),
                    new PlayerKitsProvider()
            );
        }
    }





    private static float getTrueDamage(LivingAttackEvent event) {
        if (!event.getSource().getMsgId().equals("fall")) {
            float armor = event.getEntity().getArmorValue();
            float toughness = 0;
            for (ItemStack armorPiece : event.getEntity().getArmorSlots()) {
                if (armorPiece.getItem() instanceof ArmorItem armorItem) {
                    toughness += armorItem.getToughness();
                }
            }
            float trueDamage = event.getAmount() * (1 - (Math.min(20, Math.max((armor / 5), armor - (armor / (2 + (toughness / 4))))) / 25));
            return trueDamage;
        }
        return event.getAmount();
    }
    private static float getTrueDamage(LivingHurtEvent event) {
        if (!event.getSource().getMsgId().equals("fall")) {
            float armor = event.getEntity().getArmorValue();
            float toughness = 0;
            for (ItemStack armorPiece : event.getEntity().getArmorSlots()) {
                if (armorPiece.getItem() instanceof ArmorItem armorItem) {
                    toughness += armorItem.getToughness();
                }
            }
            float trueDamage = event.getAmount() * (1 - (Math.min(20, Math.max((armor / 5), armor - (armor / (2 + (toughness / 4))))) / 25));
            return trueDamage;
        }
        return event.getAmount();
    }
}