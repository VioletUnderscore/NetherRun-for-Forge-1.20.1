package net.violetunderscore.netherrun.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresData;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresDataManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class NetherRunStart {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
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
        dispatcher.register(
                Commands.literal("netherrun")
                        .then(Commands.literal("stop")
                                .executes(NetherRunStart::executeStop)
                        )
        );
        dispatcher.register(
                Commands.literal("netherrun")
                        .then(Commands.literal("ready")
                                .executes(NetherRunStart::executeReady)
                        )
        );
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



    private static void specAll(ServerLevel level) {
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            // Set each player to Spectator Mode
            player.setGameMode(GameType.SPECTATOR);
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
        context.getSource().sendFailure(Component.literal("There is no Netherrun game to be ready for."));
        return 0;
    }
}


