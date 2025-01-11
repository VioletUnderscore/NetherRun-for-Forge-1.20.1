package net.violetunderscore.netherrun.event;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.network.packets.NetherrunPlaceBlockPacket;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.network.packets.SyncNetherRunScoresPacket;
import net.violetunderscore.netherrun.network.packets.playervars.PVarSTCPacket;
import net.violetunderscore.netherrun.variables.colorEnums;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresData;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresDataManager;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKits;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKitsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;


@Mod.EventBusSubscriber(modid = NetherRun.MODID/*, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER*/)
public class ModEventBusEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerKits.class);
//        event.register(NetherRunScores.class);
    }















    // MISCELLANEOUS
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            ServerLevel overworld = event.player.level().getServer().getLevel(Level.OVERWORLD);
            NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);


            if (scoresData.isGameActive()) {
                int team = 0;
                if (Objects.equals(scoresData.getPlayer1Name(), event.player.getName().getString())) {
                    team = 1;
                }
                else if (Objects.equals(scoresData.getPlayer2Name(), event.player.getName().getString()))
                {
                    team = 2;
                }
                if (team != 0) {
                    final int teamFinal = team;
                    /*Nether Bounds*/
                    {
                        if (!(scoresData.getNetherFloor() >= scoresData.getNetherRoof())) {
                            if (event.player.position().y > scoresData.getNetherRoof() - 5) {
                                event.player.teleportTo(event.player.position().x, scoresData.getNetherRoof() - 10, event.player.position().z);
                                event.player.sendSystemMessage(Component.literal("The Nether Roof is OFF LIMITS!").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                            } else if (event.player.position().y < scoresData.getNetherFloor() + 5) {
                                event.player.teleportTo(event.player.position().x, scoresData.getNetherFloor() + 10, event.player.position().z);
                                event.player.sendSystemMessage(Component.literal("The Void is OFF LIMITS!").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                            }
                        }
                    }
                    event.player.clearFire();
                    event.player.getFoodData().setFoodLevel(16);
                    if (event.player.onGround() && !event.player.level().getBlockState(BlockPos.containing(event.player.blockPosition().getX(), event.player.blockPosition().getY() - 1, event.player.blockPosition().getZ())).is(Blocks.LAVA)) {
                        event.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                            if (!event.player.getInventory().offhand.get(0).is(kit.getKitItem(0)) || event.player.getInventory().offhand.get(0).getCount() != event.player.getInventory().offhand.get(0).getMaxStackSize()) {
                                if (kit.getKitItem(0) == Items.WHITE_CONCRETE) {
                                    event.player.getInventory().offhand.set(0, new ItemStack(colorEnums.NetherRunConcreteColor(scoresData.getSpecificTeamColor(teamFinal)), colorEnums.NetherRunConcreteColor(scoresData.getSpecificTeamColor(teamFinal)).getMaxStackSize()));
                                } else {
                                    event.player.getInventory().offhand.set(0, new ItemStack(kit.getKitItem(0), kit.getKitItem(0).getMaxStackSize()));
                                }
                            }
                            for (int v = 0; v <= 8; v++) {
                                if (!event.player.getInventory().getItem(v).is(kit.getKitItem(v + 1)) || event.player.getInventory().getItem(v).getCount() != event.player.getInventory().getItem(v).getMaxStackSize()) {
                                    event.player.getInventory().setItem(v, new ItemStack(kit.getKitItem(v + 1), kit.getKitItem(v + 1).getMaxStackSize()));
                                    if (kit.getKitItem(v + 1) == Items.WHITE_CONCRETE) {
                                        event.player.getInventory().setItem(v, new ItemStack(colorEnums.NetherRunConcreteColor(scoresData.getSpecificTeamColor(teamFinal)), colorEnums.NetherRunConcreteColor(scoresData.getSpecificTeamColor(teamFinal)).getMaxStackSize()));
                                    } else {
                                        event.player.getInventory().setItem(v, new ItemStack(kit.getKitItem(v + 1), kit.getKitItem(v + 1).getMaxStackSize()));
                                    }
                                }
                            }
                            for(int v = 9; v <= 35; v++) {
                                event.player.getInventory().setItem(v, new ItemStack(Items.AIR));
                            }
                        });
                    }
                    if (team == 1) {
                        if (scoresData.getWhosTurn() == 1) {
                            /*Detect Fortress*/
                            {
                                ServerLevel serverLevel = (ServerLevel) event.player.level();

                                boolean inFortress;
                                if (!ModList.get().isLoaded("betterfortresses")) {
                                    ResourceKey<Structure> fortressKey = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation("minecraft", "fortress"));
                                    LocationPredicate fortressPredicate = LocationPredicate.inStructure(fortressKey);
                                    inFortress = fortressPredicate.matches(serverLevel, event.player.blockPosition().getX(), event.player.blockPosition().getY(), event.player.blockPosition().getZ());
                                }
                                else {
                                    ResourceKey<Structure> yungFortressKey = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation("betterfortresses", "fortress"));
                                    LocationPredicate yungFortressPredicate = LocationPredicate.inStructure(yungFortressKey);
                                    inFortress = yungFortressPredicate.matches(serverLevel, event.player.blockPosition().getX(), event.player.blockPosition().getY(), event.player.blockPosition().getZ());
                                }

                                scoresData.setRunnerInFortress(inFortress);
                            }
                            supplyPlayer(true, event.player, scoresData);
                            try {
                                event.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                                    kit.setCanWarp(false);
                                    NetworkHandler.sendTargeted(new PVarSTCPacket(kit.getCanWarp(), kit.isWarping(), kit.getWarpTime()), (ServerPlayer) event.player);
                                });
                            } catch (NullPointerException e) {

                            }
                        } else {
                            supplyPlayer(false, event.player, scoresData);
                            try {
                                Player runner = event.player.level().getServer().getPlayerList().getPlayerByName(scoresData.getPlayer2Name());
                                event.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                                    if (kit.isWarping() && kit.getWarpTime() == 0) {
                                        kit.setWarping(false);
                                        event.player.teleportTo(kit.getWarpSpace().x, kit.getWarpSpace().y, kit.getWarpSpace().z);
                                        getHunter(scoresData, event.player.getServer()).setGameMode(GameType.SURVIVAL);
                                        BlockState pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                                        for (int yValue = -1; yValue <= 3; yValue += 1) {
                                            if (yValue == 3) {
                                                pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                                            }
                                            for (int xValue = -1; xValue <= 1; xValue += 1) {
                                                for (int zValue = -1; zValue <= 1; zValue += 1) {
                                                    BlockPos pos = BlockPos.containing(
                                                            kit.getWarpSpace().x + xValue,
                                                            kit.getWarpSpace().y + yValue,
                                                            kit.getWarpSpace().z + zValue
                                                    );
                                                    event.player.level().setBlock(pos, pBlockToPlace, 3);
                                                    NetworkHandler.sendToAllPlayers(new NetherrunPlaceBlockPacket(pos, pBlockToPlace), event.player.getServer());
                                                }
                                            }
                                            pBlockToPlace = Blocks.AIR.defaultBlockState();
                                        }
                                    }
                                    if (kit.getWarpTime() != 0) {
                                        kit.setWarpTimer(kit.getWarpTime() - 1);
                                    }
                                    kit.setCanWarp((Math.max(Math.abs(runner.position().x - event.player.position().x), Math.abs(runner.position().z - event.player.position().z)) > 50 || Math.abs(runner.position().y - event.player.position().y) > 50)
                                            && (scoresData.getSpawnTimerR() == 0 && scoresData.getSpawnTimerH() == 0 && scoresData.isRoundActive()));
                                    if (kit.getCanWarp() && kit.getKeyDown() && kit.getWarpTime() == 0) {
                                        kit.setWarp((int) Math.floor(runner.position().x), (int) Math.floor(runner.position().y), (int) Math.floor(runner.position().z), 120);
                                        kit.setWarping(true);
                                        getHunter(scoresData, event.player.getServer()).setGameMode(GameType.SPECTATOR);
                                        event.player.teleportTo(runner.position().x, runner.position().y, runner.position().z);
                                        BlockState pBlockToPlace = Blocks.MAGENTA_STAINED_GLASS.defaultBlockState();
                                        for (int yValue = -1; yValue <= 3; yValue += 1) {
                                            if (yValue == 3) {
                                                pBlockToPlace = Blocks.MAGENTA_STAINED_GLASS.defaultBlockState();
                                            }
                                            for (int xValue = -1; xValue <= 1; xValue += 1) {
                                                for (int zValue = -1; zValue <= 1; zValue += 1) {
                                                    BlockPos pos = BlockPos.containing(
                                                            kit.getWarpSpace().x + xValue,
                                                            kit.getWarpSpace().y + yValue,
                                                            kit.getWarpSpace().z + zValue
                                                    );
                                                    NetworkHandler.sendTargeted(new NetherrunPlaceBlockPacket(pos, pBlockToPlace), (ServerPlayer) event.player);
                                                }
                                            }
                                            pBlockToPlace = Blocks.AIR.defaultBlockState();
                                        }
                                        broadcastMessageToAllPlayers(event.player.getServer(), Component.literal((event.player.getName().getString() + " has fallen behind!")).withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                                        broadcastMessageToAllPlayers(event.player.getServer(), Component.literal("They are now being teleported...").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                                    }
                                    NetworkHandler.sendTargeted(new PVarSTCPacket(kit.getCanWarp(), kit.isWarping(), kit.getWarpTime()), (ServerPlayer) event.player);
                                    LOGGER.info("Can Warp: " + kit.getCanWarp() + ", Button Down: " + kit.getKeyDown() + ", Get Warp Time: " + kit.getWarpTime());
                                });
                            } catch (NullPointerException e) {
                                LOGGER.error("Failed to check hunter warp status");
                            }
                        }
                    } else if (team == 2) {
                        if (scoresData.getWhosTurn() == 2) {
                            /*Detect Fortress*/
                            {
                                ServerLevel serverLevel = (ServerLevel) event.player.level();

                                boolean inFortress;
                                if (!ModList.get().isLoaded("betterfortresses")) {
                                    ResourceKey<Structure> fortressKey = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation("minecraft", "fortress"));
                                    LocationPredicate fortressPredicate = LocationPredicate.inStructure(fortressKey);
                                    inFortress = fortressPredicate.matches(serverLevel, event.player.blockPosition().getX(), event.player.blockPosition().getY(), event.player.blockPosition().getZ());
                                }
                                else {
                                    ResourceKey<Structure> yungFortressKey = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation("betterfortresses", "fortress"));
                                    LocationPredicate yungFortressPredicate = LocationPredicate.inStructure(yungFortressKey);
                                    inFortress = yungFortressPredicate.matches(serverLevel, event.player.blockPosition().getX(), event.player.blockPosition().getY(), event.player.blockPosition().getZ());
                                }

                                scoresData.setRunnerInFortress(inFortress);
                            }
                            supplyPlayer(true, event.player, scoresData);
                            try {
                                event.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                                    kit.setCanWarp(false);
                                    NetworkHandler.sendTargeted(new PVarSTCPacket(kit.getCanWarp(), kit.isWarping(), kit.getWarpTime()), (ServerPlayer) event.player);
                                });
                            } catch (NullPointerException e) {

                            }
                        } else {
                            supplyPlayer(false, event.player, scoresData);
                            try {
                                Player runner = event.player.level().getServer().getPlayerList().getPlayerByName(scoresData.getPlayer1Name());
                                event.player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                                    if (kit.isWarping() && kit.getWarpTime() == 0) {
                                        kit.setWarping(false);
                                        event.player.teleportTo(kit.getWarpSpace().x, kit.getWarpSpace().y, kit.getWarpSpace().z);
                                        getHunter(scoresData, event.player.getServer()).setGameMode(GameType.SURVIVAL);
                                        BlockState pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                                        for (int yValue = -1; yValue <= 3; yValue += 1) {
                                            if (yValue == 3) {
                                                pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                                            }
                                            for (int xValue = -1; xValue <= 1; xValue += 1) {
                                                for (int zValue = -1; zValue <= 1; zValue += 1) {
                                                    BlockPos pos = BlockPos.containing(
                                                            kit.getWarpSpace().x + xValue,
                                                            kit.getWarpSpace().y + yValue,
                                                            kit.getWarpSpace().z + zValue
                                                    );
                                                    event.player.level().setBlock(pos, pBlockToPlace, 3);
                                                    NetworkHandler.sendToAllPlayers(new NetherrunPlaceBlockPacket(pos, pBlockToPlace), event.player.getServer());
                                                }
                                            }
                                            pBlockToPlace = Blocks.AIR.defaultBlockState();
                                        }
                                    }
                                    if (kit.getWarpTime() != 0) {
                                        kit.setWarpTimer(kit.getWarpTime() - 1);
                                    }
                                    kit.setCanWarp((Math.max(Math.abs(runner.position().x - event.player.position().x), Math.abs(runner.position().z - event.player.position().z)) > 50 || Math.abs(runner.position().y - event.player.position().y) > 50)
                                            && (scoresData.getSpawnTimerR() == 0 && scoresData.getSpawnTimerH() == 0 && scoresData.isRoundActive()));
                                    if (kit.getCanWarp() && kit.getKeyDown() && kit.getWarpTime() == 0) {
                                        kit.setWarp((int) Math.floor(runner.position().x), (int) Math.floor(runner.position().y), (int) Math.floor(runner.position().z), 120);
                                        kit.setWarping(true);
                                        getHunter(scoresData, event.player.getServer()).setGameMode(GameType.SPECTATOR);
                                        event.player.teleportTo(runner.position().x, runner.position().y, runner.position().z);
                                        BlockState pBlockToPlace = Blocks.MAGENTA_STAINED_GLASS.defaultBlockState();
                                        for (int yValue = -1; yValue <= 3; yValue += 1) {
                                            if (yValue == 3) {
                                                pBlockToPlace = Blocks.MAGENTA_STAINED_GLASS.defaultBlockState();
                                            }
                                            for (int xValue = -1; xValue <= 1; xValue += 1) {
                                                for (int zValue = -1; zValue <= 1; zValue += 1) {
                                                    BlockPos pos = BlockPos.containing(
                                                            kit.getWarpSpace().x + xValue,
                                                            kit.getWarpSpace().y + yValue,
                                                            kit.getWarpSpace().z + zValue
                                                    );
                                                    NetworkHandler.sendTargeted(new NetherrunPlaceBlockPacket(pos, pBlockToPlace), (ServerPlayer) event.player);
                                                }
                                            }
                                            pBlockToPlace = Blocks.AIR.defaultBlockState();
                                        }
                                        broadcastMessageToAllPlayers(event.player.getServer(), Component.literal((event.player.getName().getString() + " has fallen behind!")).withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                                        broadcastMessageToAllPlayers(event.player.getServer(), Component.literal("They are now being teleported...").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                                    }
                                    NetworkHandler.sendTargeted(new PVarSTCPacket(kit.getCanWarp(), kit.isWarping(), kit.getWarpTime()), (ServerPlayer) event.player);
                                    //LOGGER.info("Can Warp: " + kit.getCanWarp() + ", Button Down: " + kit.getKeyDown() + ", Get Warp Time: " + kit.getWarpTime());
                                });
                            } catch (NullPointerException e) {
                                //LOGGER.error("Failed to check hunter warp status");
                            }
                        }
                    }
                }
            }
        }

    }
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.level.dimension() == Level.OVERWORLD && !event.level.isClientSide()) {
            MinecraftServer server = event.level.getServer();
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
                                scoresData.getSpawnX(),
                                scoresData.getSpawnY(),
                                scoresData.getSpawnZ(),
                                scoresData.getColor1(),
                                scoresData.getColor2(),
                                scoresData.getNetherRoof(),
                                scoresData.getNetherFloor(),
                                scoresData.isGameActive(),
                                scoresData.isRoundActive(),
                                scoresData.isTeam1Ready(),
                                scoresData.isTeam2Ready(),
                                scoresData.isRoundJustEnded(),
                                scoresData.isGamePaused(),
                                scoresData.isRunnerInFortress(),
                                scoresData.getPlayer1Name(),
                                scoresData.getPlayer2Name()
                        ));
            });


//            LOGGER.info("t1: {}, t2: {}, trg: {}, w: {}, rt: {}, ht: {}, round: {}, whoturn: {}, x: {}, y: {}, z: {}, \n" +
//                                "color1: {}, color2: {}, roof: {}, floor: {}, gactive: {}, ractive: {}, ready1: {}, 2: {}, end: {}, p1: {}, p2: {}",
//
//                    scoresData.getTeam1Score(),
//                    scoresData.getTeam2Score(),
//                    scoresData.getTargetScore(),
//                    scoresData.getWaitingTimer(),
//                    scoresData.getSpawnTimerR(),
//                    scoresData.getSpawnTimerH(),
//                    scoresData.getRound(),
//                    scoresData.getWhosTurn(),
//                    scoresData.getSpawnX(),
//                    scoresData.getSpawnY(),
//                    scoresData.getSpawnZ(),
//                    scoresData.getColor1(),
//                    scoresData.getColor2(),
//                    scoresData.getNetherRoof(),
//                    scoresData.getNetherFloor(),
//                    scoresData.isGameActive(),
//                    scoresData.isRoundActive(),
//                    scoresData.isTeam1Ready(),
//                    scoresData.isTeam2Ready(),
//                    scoresData.isRoundJustEnded(),
//                    scoresData.getPlayer1Name(),
//                    scoresData.getPlayer2Name()
//                    );


            if (scoresData.isGameActive() && !scoresData.isGamePaused()) {
                if (scoresData.isRoundJustEnded()) {
                    scoresData.setWhosTurn(scoresData.getWhosTurn() + 1);
                    broadcastMessageToAllPlayers(
                            server,
                            Component.literal("")
                                    .append(Component.literal("==========").withStyle(ChatFormatting.GRAY))
                                    .append(Component.literal("N").withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD, ChatFormatting.UNDERLINE))
                                    .append(Component.literal("R").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD, ChatFormatting.UNDERLINE))
                                    .append(Component.literal("==========").withStyle(ChatFormatting.GRAY))
                                    .append("\n")
                    );
                    if (scoresData.getWhosTurn() > 2) {
                        broadcastMessageToAllPlayers(
                                server,
                                Component.literal("")
                                        .append(Component.literal("Round ").withStyle(ChatFormatting.WHITE))
                                        .append(Component.literal("" + scoresData.getRound()).withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD, ChatFormatting.UNDERLINE))
                                        .append(Component.literal(" is over! ").withStyle(ChatFormatting.WHITE))
                                        .append(Component.literal("(" + timeToString(scoresData.getTeam1Score()) + " - " + timeToString(scoresData.getTeam2Score()) + ")").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC))
                                        .append("\n")
                        );
                        scoresData.setWhosTurn(1);
                        scoresData.setRound(scoresData.getRound() + 1);
                        if (Math.max(scoresData.getTeam1Score(), scoresData.getTeam2Score()) > scoresData.getTargetScore()) {
                            if (scoresData.getTeam1Score() > scoresData.getTeam2Score()) {
                                broadcastMessageToAllPlayers(
                                        server,
                                        Component.literal("")
                                                .append(Component.literal(scoresData.getPlayer1Name().toUpperCase() + " WINS THE GAME!").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD, ChatFormatting.UNDERLINE))
                                                .append("\n")
                                );
                                scoresData.setGameActive(false);
                            } else if (scoresData.getTeam1Score() < scoresData.getTeam2Score()) {
                                broadcastMessageToAllPlayers(
                                        server,
                                        Component.literal("")
                                                .append(Component.literal(scoresData.getPlayer2Name().toUpperCase() + " WINS THE GAME!").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD, ChatFormatting.UNDERLINE))
                                                .append("\n")
                                );
                                scoresData.setGameActive(false);
                            } else
                                broadcastMessageToAllPlayers(event.level.getServer(), Component.literal(
                                        "<VioletUnderscore> HOW TF DID YOU GET THE EXACT SAME TIME?!?"));
                        }
                    }
                    if (scoresData.isGameActive()) {
                        broadcastMessageToAllPlayers(
                                server,
                                Component.literal("")
                                        .append(Component.literal("Starting Round " + (scoresData.getRound()) + "...").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC))
                                        .append("\n")
                        );
                        if (scoresData.getWhosTurn() == 1) {
                            broadcastMessageToAllPlayers(
                                    server,
                                    Component.literal("")
                                            .append(Component.literal("Your turn, ").withStyle(ChatFormatting.WHITE))
                                            .append(Component.literal(scoresData.getPlayer1Name()).withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC))
                                            .append(Component.literal("!").withStyle(ChatFormatting.WHITE))
                                            .append("\n")
                            );
                        }
                        else if (scoresData.getWhosTurn() == 2) {
                            broadcastMessageToAllPlayers(
                                    server,
                                    Component.literal("")
                                            .append(Component.literal("Your turn, ").withStyle(ChatFormatting.WHITE))
                                            .append(Component.literal(scoresData.getPlayer2Name()).withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC))
                                            .append(Component.literal("!").withStyle(ChatFormatting.WHITE))
                                            .append("\n")
                            );
                        }
                        else {
                            broadcastMessageToAllPlayers(event.level.getServer(), Component.literal(
                                    "Uhhh, the variable says it's... neither of your turns? suffice to say, something is going wrong... (" +
                                    scoresData.getWhosTurn() + ")"));
                        }
                    }
                    scoresData.setRoundJustEnded(false);
                    broadcastMessageToAllPlayers(
                            server,
                            Component.literal("")
                                    .append(Component.literal("======================").withStyle(ChatFormatting.GRAY))
                    );
                }
                if (scoresData.isRoundActive()) {

                    if (scoresData.getSpawnTimerR() != 0) {
                        scoresData.setSpawnTimerR(scoresData.getSpawnTimerR() - 1);
                        if (scoresData.getSpawnTimerR() == 0) {
                            //SPAWN RUNNER

                            getRunner(scoresData, server).setGameMode(GameType.SURVIVAL);
                            getRunner(scoresData, server).setHealth(20);
                            getRunner(scoresData, server).teleportTo(
                                    getRunner(scoresData, server).position().x,
                                    getRunner(scoresData, server).position().y,
                                    getRunner(scoresData, server).position().z
                            );
                            scoresData.setSpawnX((int) Math.floor(getRunner(scoresData, server).position().x));
                            scoresData.setSpawnY((int) Math.floor(getRunner(scoresData, server).position().y));
                            scoresData.setSpawnZ((int) Math.floor(getRunner(scoresData, server).position().z));

                            BlockState pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                            for (int yValue = -1; yValue <= 3; yValue += 1) {
                                if (yValue == 3) {
                                    pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                                }
                                for (int xValue = -1; xValue <= 1; xValue += 1) {
                                    for (int zValue = -1; zValue <= 1; zValue += 1) {
                                        BlockPos pos = BlockPos.containing(
                                                scoresData.getSpawnX() + xValue,
                                                scoresData.getSpawnY() + yValue,
                                                scoresData.getSpawnZ() + zValue
                                        );
                                        getRunner(scoresData, server).serverLevel().setBlock(pos, pBlockToPlace, 3);
                                        NetworkHandler.sendToAllPlayers(new NetherrunPlaceBlockPacket(pos, pBlockToPlace), serverLevel.getServer());
                                    }
                                }
                                pBlockToPlace = Blocks.AIR.defaultBlockState();
                            }
                            getHunter(scoresData, server).setGameMode(GameType.SPECTATOR);
                            getHunter(scoresData, server).teleportTo(
                                    scoresData.getSpawnX(),
                                    scoresData.getSpawnY(),
                                    scoresData.getSpawnZ()
                            );
                            scoresData.setSpawnTimerH(120);
                        }
                    } else if (scoresData.getSpawnTimerH() != 0) {
                        scoresData.setSpawnTimerH(scoresData.getSpawnTimerH() - 1);
                        if (scoresData.getSpawnTimerH() == 0) {
                            //SPAWN HUNTER
                            getHunter(scoresData, server).setGameMode(GameType.SURVIVAL);
                            getHunter(scoresData, server).setHealth(20);
                            getHunter(scoresData, server).teleportTo(
                                    scoresData.getSpawnX(),
                                    scoresData.getSpawnY(),
                                    scoresData.getSpawnZ()
                            );

                            BlockState pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                            for (int yValue = -1; yValue <= 3; yValue += 1) {
                                if (yValue == 3) {
                                    pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                                }
                                for (int xValue = -1; xValue <= 1; xValue += 1) {
                                    for (int zValue = -1; zValue <= 1; zValue += 1) {
                                        BlockPos pos = BlockPos.containing(
                                                scoresData.getSpawnX() + xValue,
                                                scoresData.getSpawnY() + yValue,
                                                scoresData.getSpawnZ() + zValue
                                        );
                                        serverLevel.setBlock(pos, pBlockToPlace, 3);
                                        NetworkHandler.sendToAllPlayers(new NetherrunPlaceBlockPacket(pos, pBlockToPlace), serverLevel.getServer());
                                    }
                                }
                                pBlockToPlace = Blocks.AIR.defaultBlockState();
                            }
                        }
                    } else
                        if (scoresData.getWhosTurn() == 1) {
                            scoresData.setTeam1Score(scoresData.getTeam1Score() + 1);
                        } else if (scoresData.getWhosTurn() == 2) {
                            scoresData.setTeam2Score(scoresData.getTeam2Score() + 1);
                        } else {
                            LOGGER.error("Global Variable \"whosTurn\" is neither 1 nor 2 - violetunderscore.netherrun.client.ModEventBusEvents/onWorldTick:216 (or somewhere around there)");
                        }
                }
                else if (scoresData.isTeam1Ready() && scoresData.isTeam2Ready()) {
                    scoresData.setRoundActive(true);
                    scoresData.setSpawnTimerR(10 * 40);
                    teleAll(server, new Vec3(new Random().nextInt(20000) - 10000, 100, new Random().nextInt(20000) - 10000)); /* not working??? */
                    specAll(server);
                    //mark hunter as hunter and runner as runner
                    scoresData.setTeam1Ready(false);
                    scoresData.setTeam2Ready(false);
                }
            }
        }
    }





    private static void broadcastMessageToAllPlayers(MinecraftServer server, Component message) {
        server.getPlayerList().broadcastSystemMessage(message, false);
    }

    private static void specAll(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // Set each player to Spectator Mode
            player.setGameMode(GameType.SPECTATOR);
        }
    }
    private static void teleAll(MinecraftServer server, Vec3 pPos) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // teleport each player
            LOGGER.info("Teleporting player {} to {}", player.getName().getString(), pPos);
            player.teleportTo(player.serverLevel(), pPos.x, pPos.y, pPos.z, player.getYRot(), player.getXRot());
        }
    }

    private static ServerPlayer getRunner (NetherRunScoresData scoresData, MinecraftServer server) {
        if (scoresData.getWhosTurn() == 1) {
            return server.getPlayerList().getPlayerByName(scoresData.getPlayer1Name());
        }
        return server.getPlayerList().getPlayerByName(scoresData.getPlayer2Name());
    }

    private static ServerPlayer getHunter (NetherRunScoresData scoresData, MinecraftServer server) {
        if (scoresData.getWhosTurn() == 2) {
            return server.getPlayerList().getPlayerByName(scoresData.getPlayer1Name());
        }
        return server.getPlayerList().getPlayerByName(scoresData.getPlayer2Name());
    }

    private static void supplyPlayer(boolean isRunner, Player player, NetherRunScoresData scoresData) {
        if (isRunner) {
            player.getInventory().armor.set(3, new ItemStack(Items.DIAMOND_HELMET));
            player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
            player.getInventory().armor.set(1, new ItemStack(Items.LEATHER_LEGGINGS));
            player.getInventory().armor.set(0, new ItemStack(Items.DIAMOND_BOOTS));
            player.setGlowingTag(true);
            if (!scoresData.isRunnerInFortress()) {
                player.heal(0.01f);
            }
        }
        else {
            player.getInventory().armor.set(3, new ItemStack(Items.NETHERITE_HELMET));
            player.getInventory().armor.set(2, new ItemStack(Items.NETHERITE_CHESTPLATE));
            player.getInventory().armor.set(1, new ItemStack(Items.NETHERITE_LEGGINGS));
            player.getInventory().armor.set(0, new ItemStack(Items.NETHERITE_BOOTS));
            player.setGlowingTag(false);
        }
    }

    public static String timeToString(int time) {
        if (time > 118800) {
            return "99+";
        }
        return (""
                + (int)(Math.floor(time / 24000) - (Math.floor(time / 240000) * 10))
                + (int)(Math.floor(time / 2400) - (Math.floor(time / 24000) * 10))
                + ":"
                + (int)Math.floor((Math.floor(time / 40) - (Math.floor((time / 40) / 60) * 60)) / 10)
                + (int)((Math.floor(time / 40) - (Math.floor((time / 40) / 60) * 60)) - ((Math.floor((Math.floor(time / 40) - (Math.floor((time / 40) / 60) * 60)) / 10)) * 10))
        );
    }
}
