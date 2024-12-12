package net.violetunderscore.netherrun.variables.global.scores;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetherRunScoresData extends SavedData {
    private static final Logger LOGGER = LogManager.getLogger();

    int targetScore;
    int team2Score;
    int team1Score;

    //nbt keys
    private static final String TARGET_KEY = "targetScore";
    private static final String TEAM1_KEY = "team1Score";
    private static final String TEAM2_KEY = "team2Score";

    public NetherRunScoresData() {
        this.targetScore = 12000;
        this.team1Score = 24000;
        this.team2Score = 48000;
    }

    public int getTeam1Score() {return team1Score;} /*  -  -  -  -  -  -  -  */ public void setTeam1Score(int score) {this.team1Score = score; setDirty();}

    public int getTeam2Score() {return team2Score;} /*  -  -  -  -  -  -  -  */ public void setTeam2Score(int score) {this.team2Score = score; setDirty();}

    public int getTargetScore() {return targetScore;} /*  -  -  -  -  -  -  -  */ public void setTargetScore(int score) {this.targetScore = score; setDirty();}


    @Override
    public CompoundTag save(CompoundTag tag) {
        LOGGER.info("[WorldSavedData] Saving data: {}, {}, {}", team1Score, team2Score, targetScore);
        tag.putInt(TEAM1_KEY, team1Score);
        tag.putInt(TEAM2_KEY, team2Score);
        tag.putInt(TARGET_KEY, targetScore);
        return tag;
    }

    public static NetherRunScoresData load(CompoundTag tag) {
        NetherRunScoresData data = new NetherRunScoresData();
        data.team1Score = tag.getInt(TEAM1_KEY);
        data.team2Score = tag.getInt(TEAM2_KEY);
        data.targetScore = tag.getInt(TARGET_KEY);
        LOGGER.info("[WorldSavedData] Loaded data: {}, {}, {}", data.team1Score, data.team2Score, data.targetScore);
        return data;
    }
}
