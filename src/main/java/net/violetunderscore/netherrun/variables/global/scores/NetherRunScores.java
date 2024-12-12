package net.violetunderscore.netherrun.variables.global.scores;

import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetherRunScores {
    private static final Logger LOGGER = LogManager.getLogger();
    private int team1Score;
    private int team2Score;
    private int targetScore;

    public int getScore(int team) {
        return switch (team) {
            case 1 -> team1Score;
            case 2 -> team2Score;
            default -> targetScore;
        };
    }

    public void setScore(int team, int score) {
        switch(team) {
            case 1:
                team1Score = score;
            case 2:
                team2Score = score;
            default:
                targetScore = score;
        }
    }

    public void addScore(int team, int score) {
        switch(team) {
            case 1:
                team1Score += score;
            case 2:
                team2Score += score;
            default:
                targetScore += score;
        }
    }

    public void saveNBTData(CompoundTag nbt) {
        LOGGER.info("[Handler] saveNBTData is Saving data: {}, {}, {}", team1Score, team2Score, targetScore);
        nbt.putInt("team1Score", team1Score);
        nbt.putInt("team2Score", team2Score);
        nbt.putInt("targetScore", targetScore);
        LOGGER.info("[Handler] saveNBTData Saved: " + nbt);
    }

    public void loadNBTData(CompoundTag nbt) {
        LOGGER.info("[Handler] loadNBTData is Loading data: " + nbt);
        team1Score = nbt.getInt("team1Score");
        team2Score = nbt.getInt("team2Score");
        targetScore = nbt.getInt("targetScore");
        LOGGER.info("[Handler] loadNBTData loaded {}, {}, {}", team1Score, team2Score, targetScore);
    }
}
/*
level.getCapability(NetherRunScoresProvider.NETHERRUN_SCORES).ifPresent(scores -> {

});
 */