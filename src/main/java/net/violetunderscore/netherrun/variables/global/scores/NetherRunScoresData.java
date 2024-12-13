package net.violetunderscore.netherrun.variables.global.scores;




//NetherRunScoresData scoresData = NetherRunScoresDataManager.get( serverLevel );

//scoresData.setTargetScore();

//scoresData.getTargetScore();










import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetherRunScoresData extends SavedData {
    private static final Logger LOGGER = LogManager.getLogger();

    private int targetScore;
    private int team1Score;
    private int team2Score;
    private int waitingTimer;
    private int spawnTimerR;
    private int spawnTimerH;
    private int round;
    private int whosTurn;
    private boolean gameActive;
    private boolean roundActive;
    private boolean team1Ready;
    private boolean team2Ready;
    private boolean roundJustEnded;

    // NBT keys
    private static final String TARGET_KEY = "targetScore";
    private static final String TEAM1_KEY = "team1Score";
    private static final String TEAM2_KEY = "team2Score";
    private static final String WAITING_TIMER_KEY = "waitingTimer";
    private static final String SPAWN_TIMER_R_KEY = "spawnTimerR";
    private static final String SPAWN_TIMER_H_KEY = "spawnTimerH";
    private static final String ROUND_KEY = "round";
    private static final String TURN_KEY = "whosTurn";
    private static final String GAME_ACTIVE_KEY = "gameActive";
    private static final String ROUND_ACTIVE_KEY = "roundActive";
    private static final String TEAM1_READY_KEY = "team1Ready";
    private static final String TEAM2_READY_KEY = "team2Ready";
    private static final String ROUND_ENDED_KEY = "roundJustEnded";

    public NetherRunScoresData() {
        this.targetScore = 12000;
        this.team1Score = 24000;
        this.team2Score = 48000;
        this.waitingTimer = 0;
        this.spawnTimerR = 0;
        this.spawnTimerH = 0;
        this.round = 0;
        this.whosTurn = 0;
        this.gameActive = false;
        this.roundActive = false;
        this.team1Ready = false;
        this.team2Ready = false;
        this.roundJustEnded = false;
    }

    // Getter and Setter methods for all fields

    public int getTeam1Score() { return team1Score; } /*  -  -  -  -  -  -  -  */ public void setTeam1Score(int score) { this.team1Score = score; setDirty(); }
    public int getTeam2Score() { return team2Score; } /*  -  -  -  -  -  -  -  */ public void setTeam2Score(int score) { this.team2Score = score; setDirty(); }
    public int getTargetScore() { return targetScore; } /*  -  -  -  -  -  -  -  */ public void setTargetScore(int score) { this.targetScore = score; setDirty(); }
    public int getWaitingTimer() { return waitingTimer; } /*  -  -  -  -  -  -  -  */ public void setWaitingTimer(int waitingTimer) { this.waitingTimer = waitingTimer; setDirty(); }
    public int getSpawnTimerR() { return spawnTimerR; } /*  -  -  -  -  -  -  -  */ public void setSpawnTimerR(int spawnTimerR) { this.spawnTimerR = spawnTimerR; setDirty(); }
    public int getSpawnTimerH() { return spawnTimerH; } /*  -  -  -  -  -  -  -  */ public void setSpawnTimerH(int spawnTimerH) { this.spawnTimerH = spawnTimerH; setDirty(); }
    public int getRound() { return round; } /*  -  -  -  -  -  -  -  */ public void setRound(int round) { this.round = round; setDirty(); }
    public int getWhosTurn() { return whosTurn; } /*  -  -  -  -  -  -  -  */ public void setWhosTurn(int whosTurn) { this.whosTurn = whosTurn; setDirty(); }

    public boolean isGameActive() { return gameActive; } /*  -  -  -  -  -  -  -  */ public void setGameActive(boolean gameActive) { this.gameActive = gameActive; setDirty(); }
    public boolean isRoundActive() { return roundActive; } /*  -  -  -  -  -  -  -  */ public void setRoundActive(boolean roundActive) { this.roundActive = roundActive; setDirty(); }
    public boolean isTeam1Ready() { return team1Ready; } /*  -  -  -  -  -  -  -  */ public void setTeam1Ready(boolean team1Ready) { this.team1Ready = team1Ready; setDirty(); }
    public boolean isTeam2Ready() { return team2Ready; } /*  -  -  -  -  -  -  -  */ public void setTeam2Ready(boolean team2Ready) { this.team2Ready = team2Ready; setDirty(); }
    public boolean isRoundJustEnded() { return roundJustEnded; } /*  -  -  -  -  -  -  -  */ public void setRoundJustEnded(boolean roundJustEnded) { this.roundJustEnded = roundJustEnded; setDirty(); }

    @Override
    public CompoundTag save(CompoundTag tag) {
        LOGGER.info("[WorldSavedData] Saving data: team1={}, team2={}, target={}", team1Score, team2Score, targetScore);
        tag.putInt(TEAM1_KEY, team1Score);
        tag.putInt(TEAM2_KEY, team2Score);
        tag.putInt(TARGET_KEY, targetScore);
        tag.putInt(WAITING_TIMER_KEY, waitingTimer);
        tag.putInt(SPAWN_TIMER_R_KEY, spawnTimerR);
        tag.putInt(SPAWN_TIMER_H_KEY, spawnTimerH);
        tag.putInt(ROUND_KEY, round);
        tag.putInt(TURN_KEY, whosTurn);
        tag.putBoolean(GAME_ACTIVE_KEY, gameActive);
        tag.putBoolean(ROUND_ACTIVE_KEY, roundActive);
        tag.putBoolean(TEAM1_READY_KEY, team1Ready);
        tag.putBoolean(TEAM2_READY_KEY, team2Ready);
        tag.putBoolean(ROUND_ENDED_KEY, roundJustEnded);
        return tag;
    }

    public static NetherRunScoresData load(CompoundTag tag) {
        NetherRunScoresData data = new NetherRunScoresData();
        data.team1Score = tag.getInt(TEAM1_KEY);
        data.team2Score = tag.getInt(TEAM2_KEY);
        data.targetScore = tag.getInt(TARGET_KEY);
        data.waitingTimer = tag.getInt(WAITING_TIMER_KEY);
        data.spawnTimerR = tag.getInt(SPAWN_TIMER_R_KEY);
        data.spawnTimerH = tag.getInt(SPAWN_TIMER_H_KEY);
        data.round = tag.getInt(ROUND_KEY);
        data.whosTurn = tag.getInt(TURN_KEY);
        data.gameActive = tag.getBoolean(GAME_ACTIVE_KEY);
        data.roundActive = tag.getBoolean(ROUND_ACTIVE_KEY);
        data.team1Ready = tag.getBoolean(TEAM1_READY_KEY);
        data.team2Ready = tag.getBoolean(TEAM2_READY_KEY);
        data.roundJustEnded = tag.getBoolean(ROUND_ENDED_KEY);
        LOGGER.info("[WorldSavedData] Loaded data: team1={}, team2={}, target={} round={} turn={} gameActive={}",
                data.team1Score, data.team2Score, data.targetScore, data.round, data.whosTurn, data.gameActive);
        return data;
    }
}





