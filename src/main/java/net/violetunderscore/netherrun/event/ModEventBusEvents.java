package net.violetunderscore.netherrun.event;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.client.NetherRunScoresDisplay;
import net.violetunderscore.netherrun.commands.NetherRunStart;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.network.SyncNetherRunScoresPacket;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresData;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresDataManager;
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
//        event.register(NetherRunScores.class);
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
//    @SubscribeEvent
//    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<Level> event) {
//        if (event.getObject().dimension() == Level.OVERWORLD) {
//            if (!event.getObject().getCapability(NetherRunScoresProvider.NETHERRUN_SCORES).isPresent()) {
//                LOGGER.info("Attaching NetherRunScores capability to Overworld");
//                event.addCapability(new ResourceLocation(NetherRun.MODID, "scores"), new NetherRunScoresProvider());
//            }
//        }
//    }





    // MISCELLANEOUS
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            NetherRunScoresData scoresData = NetherRunScoresDataManager.get((ServerLevel) event.player.level());
            if (scoresData.isGameActive()) {
                event.player.clearFire();
                event.player.getFoodData().setFoodLevel(16);
                event.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                    if (!event.player.getInventory().offhand.get(0).is(kit.getKitItem(0)) || event.player.getInventory().offhand.get(0).getCount() != event.player.getInventory().offhand.get(0).getMaxStackSize()) {
                        event.player.getInventory().offhand.set(0, new ItemStack(kit.getKitItem(0), kit.getKitItem(0).getMaxStackSize()));
                    }
                    for (int v = 0; v <= 8; v++) {
                        if (!event.player.getInventory().getItem(v).is(kit.getKitItem(v + 1)) || event.player.getInventory().getItem(v).getCount() != event.player.getInventory().getItem(v).getMaxStackSize()) {
                            event.player.getInventory().setItem(v, new ItemStack(kit.getKitItem(v + 1), kit.getKitItem(v + 1).getMaxStackSize()));
                        }
                    }
                });
            }
        }

    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.level.dimension() == Level.OVERWORLD && !event.level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) event.level;
            NetherRunScoresData scoresData = NetherRunScoresDataManager.get(serverLevel);


            serverLevel.getServer().execute(() -> {
                NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                        new SyncNetherRunScoresPacket(
                                scoresData.getTeam1Score(),
                                scoresData.getTeam2Score(),
                                scoresData.getTargetScore(),
                                scoresData.getWaitingTimer(),
                                scoresData.getSpawnTimerR(),
                                scoresData.getSpawnTimerH(),
                                scoresData.getRound(),
                                scoresData.getWhosTurn(),
                                scoresData.isGameActive(),
                                scoresData.isRoundActive(),
                                scoresData.isTeam1Ready(),
                                scoresData.isTeam2Ready(),
                                scoresData.isRoundJustEnded()));
            });


            LOGGER.info("t1: {}, t2: {}, trg: {}, w: {}, rt: {}, ht: {}, round: {}, whoturn: {}, gactive: {}, ractive: {}, ready1: {}, 2: {}, end: {}",
                    scoresData.getTeam1Score(),
                    scoresData.getTeam2Score(),
                    scoresData.getTargetScore(),
                    scoresData.getWaitingTimer(),
                    scoresData.getSpawnTimerR(),
                    scoresData.getSpawnTimerH(),
                    scoresData.getRound(),
                    scoresData.getWhosTurn(),
                    scoresData.isGameActive(),
                    scoresData.isRoundActive(),
                    scoresData.isTeam1Ready(),
                    scoresData.isTeam2Ready(),
                    scoresData.isRoundJustEnded() );


            if (scoresData.isGameActive()) {
                if (scoresData.isRoundJustEnded()) {
                    scoresData.setWhosTurn(scoresData.getWhosTurn() + 1);
                    if (scoresData.getWhosTurn() > 2) {
                        broadcastMessageToAllPlayers(event.level.getServer(), Component.literal(
                                "Round " + scoresData.getRound() + " is over! (" + NetherRunScoresDisplay.timeToString(scoresData.getTeam1Score()) + " - " + NetherRunScoresDisplay.timeToString(scoresData.getTeam2Score()) + ")"));
                        scoresData.setWhosTurn(1);
                        scoresData.setRound(scoresData.getRound() + 1);
                        if (Math.max(scoresData.getTeam1Score(), scoresData.getTeam2Score()) > scoresData.getTargetScore()) {
                            if (scoresData.getTeam1Score() > scoresData.getTeam2Score()) {
                                broadcastMessageToAllPlayers(event.level.getServer(), Component.literal(
                                        "Team 1 Wins the game!"));
                                scoresData.setGameActive(false);
                            } else if (scoresData.getTeam1Score() < scoresData.getTeam2Score()) {
                                broadcastMessageToAllPlayers(event.level.getServer(), Component.literal(
                                        "Team 2 Wins the game!"));
                                scoresData.setGameActive(false);
                            } else
                                broadcastMessageToAllPlayers(event.level.getServer(), Component.literal(
                                        "HOW TF DID YOU GET THE EXACT SAME TIME?"));
                        }
                        broadcastMessageToAllPlayers(event.level.getServer(), Component.literal(
                                "Starting Round " + (scoresData.getRound()) + "..."));
                    }
                    if (scoresData.isGameActive()) {
                        broadcastMessageToAllPlayers(event.level.getServer(), Component.literal(
                                "Your turn, player " + (scoresData.getWhosTurn()) + "!"));
                    }
                    scoresData.setRoundJustEnded(false);
                }
                if (scoresData.isRoundActive()) {
                    if (scoresData.getSpawnTimerR() != 0) {
                        scoresData.setSpawnTimerR(scoresData.getSpawnTimerR() - 1);
                        if (scoresData.getSpawnTimerR() == 0) {
                            //spawn runner
                            scoresData.setSpawnTimerH(200);
                        }
                    } else if (scoresData.getSpawnTimerH() != 0) {
                        scoresData.setSpawnTimerH(scoresData.getSpawnTimerH() - 1);
                        if (scoresData.getSpawnTimerH() == 0) {
                            //spawn hunter
                        }
                    } else
                        if (scoresData.getWhosTurn() == 1) {
                            scoresData.setTeam1Score(scoresData.getTeam1Score() + 1);
                        } else if (scoresData.getWhosTurn() == 2) {
                            scoresData.setTeam2Score(scoresData.getTeam2Score() + 1);
                        } else {
                            LOGGER.error("If returned else - violetunderscore.netherrun.client.ModEventBusEvents/onWorldTick:201");
                        }
                }
                else if (scoresData.isTeam1Ready() && scoresData.isTeam2Ready()) {
                    scoresData.setRoundActive(true);
                    scoresData.setSpawnTimerR(600);
                    //tp all to random location
                    specAll(((ServerLevel) event.level).getLevel());
                    //mark hunter as hunter and runner as runner
                    scoresData.setTeam1Ready(false);
                    scoresData.setTeam2Ready(false);
                }
            }
        }
    }

    private static void broadcastMessageToAllPlayers(MinecraftServer server, Component message) {
        server.getPlayerList().broadcastSystemMessage(message, false); // The 'false' means it's not a system message
    }

    private static void specAll(ServerLevel level) {
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            // Set each player to Spectator Mode
            player.setGameMode(GameType.SPECTATOR);
        }
    }


}
