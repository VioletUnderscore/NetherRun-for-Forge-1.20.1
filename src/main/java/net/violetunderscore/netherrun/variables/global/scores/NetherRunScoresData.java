package net.violetunderscore.netherrun.variables.global.scores;





//NetherRunScoresData scoresData = NetherRunScoresDataManager.get( serverLevel );
//scoresData.setTargetScore();
//scoresData.getTargetScore();





//  To add a variable, we must alter all of these classes
//      violetunderscore.netherrun.variables.scores.NetherRunScoresData
//      violetunderscore.netherrun.network.SyncNetherRunScoresPacket
//      violetunderscore.netherrun.client.NetherRunGlobalClientData
//      violetunderscore.netherrun.event.ModEventBusEvents [onWorldTick]





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
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private int color1;
    private int color2;
    private int netherRoof;
    private int netherFloor;
    private boolean gameActive;
    private boolean roundActive;
    private boolean team1Ready;
    private boolean team2Ready;
    private boolean roundJustEnded;
    private String player1Name;
    private String player2Name;

    // NBT keys
    private static final String TARGET_KEY = "targetScore";
    private static final String TEAM1_KEY = "team1Score";
    private static final String TEAM2_KEY = "team2Score";
    private static final String WAITING_TIMER_KEY = "waitingTimer";
    private static final String SPAWN_TIMER_R_KEY = "spawnTimerR";
    private static final String SPAWN_TIMER_H_KEY = "spawnTimerH";
    private static final String ROUND_KEY = "round";
    private static final String TURN_KEY = "whosTurn";
    private static final String SPAWN_X_KEY = "spawnX";
    private static final String SPAWN_Y_KEY = "spawnY";
    private static final String SPAWN_Z_KEY = "spawnZ";
    private static final String COLOR_1_KEY = "color1";
    private static final String COLOR_2_KEY = "color2";
    private static final String NETHER_ROOF_KEY = "netherRoof";
    private static final String NETHER_FLOOR_KEY = "netherFloor";
    private static final String GAME_ACTIVE_KEY = "gameActive";
    private static final String ROUND_ACTIVE_KEY = "roundActive";
    private static final String TEAM1_READY_KEY = "team1Ready";
    private static final String TEAM2_READY_KEY = "team2Ready";
    private static final String ROUND_ENDED_KEY = "roundJustEnded";
    private static final String TEAM1_PLAYER_KEY = "player1Name";
    private static final String TEAM2_PLAYER_KEY = "player2Name";

    public NetherRunScoresData() {
        this.targetScore = 0;
        this.team1Score = 0;
        this.team2Score = 0;
        this.waitingTimer = 0;
        this.spawnTimerR = 0;
        this.spawnTimerH = 0;
        this.round = 0;
        this.whosTurn = 0;
        this.spawnX = 0;
        this.spawnY = 0;
        this.spawnZ = 0;
        this.color1 = 1;
        this.color2 = 5;
        this.netherRoof = 0;
        this.netherFloor = 128;
        this.gameActive = false;
        this.roundActive = false;
        this.team1Ready = false;
        this.team2Ready = false;
        this.roundJustEnded = false;
        this.player1Name = "";
        this.player2Name = "";
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
    public int getSpawnX() { return spawnX; } /*  -  -  -  -  -  -  -  */ public void setSpawnX(int spawnX) { this.spawnX = spawnX; setDirty(); }
    public int getSpawnY() { return spawnY; } /*  -  -  -  -  -  -  -  */ public void setSpawnY(int spawnY) { this.spawnY = spawnY; setDirty(); }
    public int getSpawnZ() { return spawnZ; } /*  -  -  -  -  -  -  -  */ public void setSpawnZ(int spawnZ) { this.spawnZ = spawnZ; setDirty(); }
    public int getColor1() { return color1; } /*  -  -  -  -  -  -  -  */ public void setColor1(int color1) { this.color1 = color1; setDirty(); }
    public int getColor2() { return color2; } /*  -  -  -  -  -  -  -  */ public void setColor2(int color2) { this.color2 = color2; setDirty(); }
    public int getNetherRoof() { return netherRoof; } /*  -  -  -  -  -  -  -  */ public void setNetherRoof(int netherRoof) { this.netherRoof = netherRoof; setDirty(); }
    public int getNetherFloor() { return netherFloor; } /*  -  -  -  -  -  -  -  */ public void setNetherFloor(int netherFloor) { this.netherFloor = netherFloor; setDirty(); }

    public boolean isGameActive() { return gameActive; } /*  -  -  -  -  -  -  -  */ public void setGameActive(boolean gameActive) { this.gameActive = gameActive; setDirty(); }
    public boolean isRoundActive() { return roundActive; } /*  -  -  -  -  -  -  -  */ public void setRoundActive(boolean roundActive) { this.roundActive = roundActive; setDirty(); }
    public boolean isTeam1Ready() { return team1Ready; } /*  -  -  -  -  -  -  -  */ public void setTeam1Ready(boolean team1Ready) { this.team1Ready = team1Ready; setDirty(); }
    public boolean isTeam2Ready() { return team2Ready; } /*  -  -  -  -  -  -  -  */ public void setTeam2Ready(boolean team2Ready) { this.team2Ready = team2Ready; setDirty(); }
    public boolean isRoundJustEnded() { return roundJustEnded; } /*  -  -  -  -  -  -  -  */ public void setRoundJustEnded(boolean roundJustEnded) { this.roundJustEnded = roundJustEnded; setDirty(); }

    public String getPlayer1Name() { return player1Name; } /*  -  -  -  -  -  -  -  */ public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; setDirty(); }
    public String getPlayer2Name() { return player2Name; } /*  -  -  -  -  -  -  -  */ public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; setDirty(); }

    public int getSpecificTeamColor (int team) {
        if (team == 1) {
            return color1;
        }
        if (team == 2) {
            return color2;
        }
        return -1;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt(TEAM1_KEY, team1Score);
        tag.putInt(TEAM2_KEY, team2Score);
        tag.putInt(TARGET_KEY, targetScore);
        tag.putInt(WAITING_TIMER_KEY, waitingTimer);
        tag.putInt(SPAWN_TIMER_R_KEY, spawnTimerR);
        tag.putInt(SPAWN_TIMER_H_KEY, spawnTimerH);
        tag.putInt(ROUND_KEY, round);
        tag.putInt(TURN_KEY, whosTurn);
        tag.putInt(SPAWN_X_KEY, spawnX);
        tag.putInt(SPAWN_Y_KEY, spawnY);
        tag.putInt(SPAWN_Z_KEY, spawnZ);
        tag.putInt(COLOR_1_KEY, color1);
        tag.putInt(COLOR_2_KEY, color2);
        tag.putInt(NETHER_ROOF_KEY, netherRoof);
        tag.putInt(NETHER_FLOOR_KEY, netherFloor);
        tag.putBoolean(GAME_ACTIVE_KEY, gameActive);
        tag.putBoolean(ROUND_ACTIVE_KEY, roundActive);
        tag.putBoolean(TEAM1_READY_KEY, team1Ready);
        tag.putBoolean(TEAM2_READY_KEY, team2Ready);
        tag.putBoolean(ROUND_ENDED_KEY, roundJustEnded);
        tag.putString(TEAM1_PLAYER_KEY, player1Name);
        tag.putString(TEAM2_PLAYER_KEY, player2Name);
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
        data.spawnX = tag.getInt(SPAWN_X_KEY);
        data.spawnY = tag.getInt(SPAWN_Y_KEY);
        data.spawnZ = tag.getInt(SPAWN_Z_KEY);
        data.color1 = tag.getInt(COLOR_1_KEY);
        data.color2 = tag.getInt(COLOR_2_KEY);
        data.netherRoof = tag.getInt(NETHER_ROOF_KEY);
        data.netherFloor = tag.getInt(NETHER_FLOOR_KEY);
        data.gameActive = tag.getBoolean(GAME_ACTIVE_KEY);
        data.roundActive = tag.getBoolean(ROUND_ACTIVE_KEY);
        data.team1Ready = tag.getBoolean(TEAM1_READY_KEY);
        data.team2Ready = tag.getBoolean(TEAM2_READY_KEY);
        data.roundJustEnded = tag.getBoolean(ROUND_ENDED_KEY);
        data.player1Name = tag.getString(TEAM1_PLAYER_KEY);
        data.player2Name = tag.getString(TEAM2_PLAYER_KEY);
        return data;
    }
}





