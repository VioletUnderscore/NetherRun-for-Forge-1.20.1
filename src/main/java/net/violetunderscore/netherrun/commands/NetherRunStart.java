package net.violetunderscore.netherrun.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.violetunderscore.netherrun.item.ItemFetcher;
import net.violetunderscore.netherrun.variables.colorEnums;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresData;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresDataManager;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKitsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class NetherRunStart {
    public static final Logger LOGGER = LogManager.getLogger();
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        /*start*/{
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("start")
                                    .then(Commands.argument("player1", EntityArgument.player())
                                            .then(Commands.argument("player2", EntityArgument.player())
                                                    .then(Commands.argument("Game Length", IntegerArgumentType.integer())
                                                            .then(Commands.literal("minutes")
                                                                    .executes(NetherRunStart::executeStartInMinutes)
                                                            )
                                                    )
                                            )
                                    )
                            )
            );
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("start")
                                    .then(Commands.argument("player1", EntityArgument.player())
                                            .then(Commands.argument("player2", EntityArgument.player())
                                                    .then(Commands.argument("Game Length", IntegerArgumentType.integer())
                                                            .then(Commands.literal("seconds")
                                                                    .executes(NetherRunStart::executeStartInSeconds)
                                                            )
                                                    )
                                            )
                                    )
                            )
            );
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("start")
                                    .then(Commands.argument("player1", EntityArgument.player())
                                            .then(Commands.argument("player2", EntityArgument.player())
                                                    .then(Commands.argument("Game Length", IntegerArgumentType.integer())
                                                            .then(Commands.literal("ticks")
                                                                    .executes(NetherRunStart::executeStartInTicks)
                                                            )
                                                    )
                                            )
                                    )
                            )
            );
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("start")
                                    .then(Commands.argument("player1", EntityArgument.player())
                                            .then(Commands.argument("player2", EntityArgument.player())
                                                    .then(Commands.argument("Game Length", IntegerArgumentType.integer())
                                                            .executes(NetherRunStart::executeStartInMinutes)
                                                    )
                                            )
                                    )
                            )
            );
        }
        /*end*/{
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("stop")
                                    .executes(NetherRunStart::executeStop)
                            )
            );
        }
        /*ready*/{
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("ready")
                                    .executes(NetherRunStart::executeReady)
                            )
            );
        }
        /*bounds*/{
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("bounds")
                                    .then(Commands.literal("roof")
                                            .then(Commands.argument("Nether Roof Y Coord", IntegerArgumentType.integer())
                                                    .executes(NetherRunStart::executeRoofLimit)
                                            )
                                    )
                            )
            );
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("bounds")
                                    .then(Commands.literal("floor")
                                            .then(Commands.argument("Nether Floor Y Coord", IntegerArgumentType.integer())
                                                    .executes(NetherRunStart::executeFloorLimit)
                                            )
                                    )
                            )
            );
        }
        /*color*/{
            LiteralArgumentBuilder<CommandSourceStack> netherrunCommand = Commands.literal("netherrun")
                    .then(Commands.literal("color"));

            for (colorEnums.NetherRunColors color : colorEnums.NetherRunColors.values()) {
                netherrunCommand.then(
                        Commands.literal("color")
                                .then(Commands.literal(color.getName())
                                                .executes(context -> executeColorChange(context, color.getId()))
                                )
                );
            }

            dispatcher.register(netherrunCommand);
        }
        /*kits*/{
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("kit")
                                    .then(Commands.literal("load")
                                            .executes(NetherRunStart::executeKitLoad)
                                    )
                            )
            );
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("kit")
                                    .then(Commands.literal("save")
                                            .executes(NetherRunStart::executeKitSave)
                                    )
                            )
            );
        }
        /*help*/{
            dispatcher.register(
                    Commands.literal("netherrun")
                            .then(Commands.literal("help")
                                    .executes(NetherRunStart::executeHelp)
                            )
            );
        }
    }

    private static int executeStart(CommandContext<CommandSourceStack> context, int measurement) {

        ServerLevel overworld = context.getSource().getServer().getLevel(Level.OVERWORLD);
        NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);

        if (!scoresData.isGameActive()) {
            Player player1;
            try {
                player1 = EntityArgument.getPlayer(context, "player1");
            } catch (Exception e) {
                // Handle exception or invalid argument input gracefully.
                context.getSource().sendFailure(Component.literal("Invalid player specified."));
                return 0;
            }
            Player player2;
            try {
                player2 = EntityArgument.getPlayer(context, "player2");
            } catch (Exception e) {
                // Handle exception or invalid argument input gracefully.
                context.getSource().sendFailure(Component.literal("Invalid player specified."));
                return 0;
            }

            if (player1 == player2) {
                context.getSource().sendFailure(Component.literal("Player1 can not be Player2."));
                return 0;
            }
            int time = IntegerArgumentType.getInteger(context, "Game Length");

            context.getSource().sendSuccess(() -> Component.literal("NETHER RUN STARTING!"), false);

            specAll(overworld);
            scoresData.setTeam1Score(0);
            scoresData.setTeam2Score(0);
            scoresData.setTargetScore(time * measurement);
            scoresData.setRound(1);
            scoresData.setWhosTurn(1);
            scoresData.setGameActive(true);
            scoresData.setRoundActive(false);
            scoresData.setTeam1Ready(false);
            scoresData.setTeam2Ready(false);
            scoresData.setPlayer1Name(player1.getName().getString());
            scoresData.setPlayer2Name(player2.getName().getString());
            return 1;
        }
        else
        {
            context.getSource().sendSuccess(() -> Component.literal("Netherrun is already running..."), false);
            return 1;
        }
    }
    private static int executeStartInMinutes(CommandContext<CommandSourceStack> context) {
        return executeStart(context, 40 * 60);
    }
    private static int executeStartInSeconds(CommandContext<CommandSourceStack> context) {
        return executeStart(context, 40);
    }
    private static int executeStartInTicks(CommandContext<CommandSourceStack> context) {
        return executeStart(context, 1);
    }



    private static int executeStop(CommandContext<CommandSourceStack> context) {
        ServerLevel overworld = context.getSource().getServer().getLevel(Level.OVERWORLD);
        NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);

        if (scoresData.isGameActive()) {
            context.getSource().sendSuccess(() -> Component.literal("NETHER RUN STOPPING!"), false);

            specAll(context.getSource().getLevel());

            scoresData.setTeam1Score(0);
            scoresData.setTeam2Score(0);
            scoresData.setTargetScore(0);
            scoresData.setGameActive(false);
            scoresData.setRoundActive(false);
            scoresData.setTeam1Ready(false);
            scoresData.setTeam2Ready(false);
            return 1;
        }
        else
        {
            context.getSource().sendSuccess(() -> Component.literal("Netherrun is already not running..."), false);
            return 1;
        }
    }



    private static int executeReady(CommandContext<CommandSourceStack> context) {
        ServerLevel overworld = context.getSource().getServer().getLevel(Level.OVERWORLD);
        NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);

        if (scoresData.isGameActive()) {
            if (scoresData.isRoundActive()) {
                context.getSource().sendFailure(Component.literal("The round has already started"));
                return 0;
            }
            else
            {
                if (Objects.equals(scoresData.getPlayer1Name(), context.getSource().getEntity().getName().getString())) {
                    scoresData.setTeam1Ready(!scoresData.isTeam1Ready());
                    if (scoresData.isTeam1Ready()) {
                        context.getSource().sendSuccess(() -> Component.literal("You are ready!"), false);
                        return 1;
                    }
                    context.getSource().sendSuccess(() -> Component.literal("You are not ready!"), false);
                    return 1;
                }
                else if (Objects.equals(scoresData.getPlayer2Name(), context.getSource().getEntity().getName().getString())) {
                    scoresData.setTeam2Ready(!scoresData.isTeam2Ready());
                    if (scoresData.isTeam2Ready()) {
                        context.getSource().sendSuccess(() -> Component.literal("You are ready!"), false);
                        return 1;
                    }
                    context.getSource().sendSuccess(() -> Component.literal("You are not ready!"), false);
                    return 1;
                }
                else {
                    context.getSource().sendFailure(Component.literal("Nobody asked for your opinion, " + context.getSource().getEntity().getName().getString() + "."));
                    return 0;
                }
            }
        }
        context.getSource().sendFailure(Component.literal("There is no NetherRun game to be ready for."));
        return 0;
    }



    private static int executeRoofLimit(CommandContext<CommandSourceStack> context) {
        ServerLevel overworld = context.getSource().getServer().getLevel(Level.OVERWORLD);
        NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);

        scoresData.setNetherRoof(IntegerArgumentType.getInteger(context, "Nether Roof Y Coord"));
        context.getSource().sendSuccess(() -> Component.literal("Nether Roof is currently " + scoresData.getNetherRoof()), false);
        context.getSource().sendSuccess(() -> Component.literal("Nether Floor is set to " + scoresData.getNetherFloor()), false);
        return 1;
    }
    private static int executeFloorLimit(CommandContext<CommandSourceStack> context) {
        ServerLevel overworld = context.getSource().getServer().getLevel(Level.OVERWORLD);
        NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);

        scoresData.setNetherFloor(IntegerArgumentType.getInteger(context, "Nether Floor Y Coord"));
        context.getSource().sendSuccess(() -> Component.literal("Nether Roof is set to " + scoresData.getNetherRoof()), false);
        context.getSource().sendSuccess(() -> Component.literal("Nether Floor is currently " + scoresData.getNetherFloor()), false);
        return 1;
    }



    private static int executeColorChange(CommandContext<CommandSourceStack> context, int colorId) {
        ServerLevel overworld = context.getSource().getServer().getLevel(Level.OVERWORLD);
        NetherRunScoresData scoresData = NetherRunScoresDataManager.get(overworld);
        if (scoresData.isGameActive()) {
            if (Objects.equals(scoresData.getPlayer1Name(), context.getSource().getEntity().getName().getString())) {
                scoresData.setColor1(colorId);
                context.getSource().sendSuccess(() -> Component.literal("Your team color is now " + colorEnums.NetherRunColors.getNameById(colorId) + "!"), false);
                return 1;
            } else if (Objects.equals(scoresData.getPlayer2Name(), context.getSource().getEntity().getName().getString())) {
                scoresData.setColor2(colorId);
                context.getSource().sendSuccess(() -> Component.literal("Your team color is now " + colorEnums.NetherRunColors.getNameById(colorId) + "!"), false);
                return 1;
            }
            context.getSource().sendFailure(Component.literal("You aren't in this game."));
            return 0;
        }
        context.getSource().sendFailure(Component.literal("There is no NetherRun game active."));
        return 0;
    }



    private static int executeKitLoad(CommandContext<CommandSourceStack> context) {
        try {
            context.getSource().getEntity().getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                Player player = (Player) context.getSource().getEntity();
                player.getInventory().offhand.set(0, new ItemStack(kit.getKitItem(0), kit.getKitItem(0).getMaxStackSize()));
                for (int v = 0; v < 9; v++) {
                    player.getInventory().setItem(v, new ItemStack(kit.getKitItem(v + 1), kit.getKitItem(v + 1).getMaxStackSize()));
                }
            });
            context.getSource().sendSuccess(() -> Component.literal("Your kit has been loaded"), false);
            return 1;
        } catch (NullPointerException e) {
            context.getSource().sendFailure(Component.literal(e.getMessage()));
            return 0;
        }
    }
    private static int executeKitSave(CommandContext<CommandSourceStack> context) {
        try {
            context.getSource().getEntity().getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                Player player = (Player) context.getSource().getEntity();
                kit.setKit(new String[]{
                        ItemFetcher.ungetItem(player.getInventory().offhand.get(0).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(0).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(1).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(2).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(3).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(4).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(5).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(6).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(7).getItem()),
                        ItemFetcher.ungetItem(player.getInventory().getItem(8).getItem()),
                });
            });
            context.getSource().sendSuccess(() -> Component.literal("Your kit has been saved"), false);
            return 1;
        } catch (NullPointerException e) {
            context.getSource().sendFailure(Component.literal(e.getMessage()));
            return 0;
        }
    }



    private static int executeHelp(CommandContext<CommandSourceStack> context) {
            context.getSource().sendSuccess(() -> Component.literal("===============NetherRun Help===============").withStyle(ChatFormatting.BOLD), false);

            context.getSource().sendSuccess(() -> Component.literal("/netherrun kit load").withStyle(ChatFormatting.BOLD)
                    .append(" - loads your kit to your hotbar").withStyle(ChatFormatting.RESET), false);
            context.getSource().sendSuccess(() -> Component.literal("/netherrun kit save").withStyle(ChatFormatting.BOLD)
                    .append(" - sets your kit to your hotbar").withStyle(ChatFormatting.RESET), false);
            context.getSource().sendSuccess(() -> Component.literal("/netherrun bounds").withStyle(ChatFormatting.BOLD)
                    .append(" - Set the nether roof and nether floor limits").withStyle(ChatFormatting.RESET), false);
            context.getSource().sendSuccess(() -> Component.literal("/netherrun start").withStyle(ChatFormatting.BOLD)
                    .append(" - starts NetherRun").withStyle(ChatFormatting.RESET), false);
            context.getSource().sendSuccess(() -> Component.literal("/netherrun stop").withStyle(ChatFormatting.BOLD)
                    .append(" - stops NetherRun").withStyle(ChatFormatting.RESET), false);
            context.getSource().sendSuccess(() -> Component.literal("/netherrun ready").withStyle(ChatFormatting.BOLD)
                    .append(" - marks you as ready").withStyle(ChatFormatting.RESET), false);
            context.getSource().sendSuccess(() -> Component.literal("/netherrun color").withStyle(ChatFormatting.BOLD)
                    .append(" - sets your team color").withStyle(ChatFormatting.RESET), false);
            return 1;
    }










    private static void specAll(ServerLevel level) {
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            // Set each player to Spectator Mode
            player.setGameMode(GameType.SPECTATOR);
        }
    }
}


