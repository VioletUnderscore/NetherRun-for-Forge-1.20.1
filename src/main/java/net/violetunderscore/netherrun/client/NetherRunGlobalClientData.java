package net.violetunderscore.netherrun.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetherRunGlobalClientData {
    private static int team1Score = 0;
    private static int team2Score = 0;
    private static int targetScore = 0;
    private static int waitingTimer = 0;
    private static int spawnTimerR = 0;
    private static int spawnTimerH = 0;
    private static int round = 0;
    private static int whosTurn = 0;
    private static int spawnX = 0;
    private static int spawnY = 0;
    private static int spawnZ = 0;
    private static int color1 = 0;
    private static int color2 = 0;
    private static int netherRoof = 0;
    private static int netherFloor = 0;
    private static boolean gameActive = false;
    private static boolean roundActive = false;
    private static boolean team1Ready = false;
    private static boolean team2Ready = false;
    private static boolean roundJustEnded = false;
    private static boolean gamePaused = false;
    private static boolean runnerInFortress = false;
    private static String player1Name = "";
    private static String player2Name = "";

    // Setters
    public static void setScore1(int score) {
        team1Score = score;
    }
    public static void setScore2(int score) {
        team2Score = score;
    }
    public static void setScoreTarget(int target) {
        targetScore = target;
    }
    public static void setWaitingTimer(int timer) {
        waitingTimer = timer;
    }
    public static void setSpawnTimerR(int timer) {
        spawnTimerR = timer;
    }
    public static void setSpawnTimerH(int timer) {
        spawnTimerH = timer;
    }
    public static void setRound(int roundNum) {
        round = roundNum;
    }
    public static void setWhosTurn(int turn) {
        whosTurn = turn;
    }
    public static void setSpawnX(int turn) {
        spawnX = turn;
    }
    public static void setSpawnY(int turn) {
        spawnY = turn;
    }
    public static void setSpawnZ(int turn) {
        spawnZ = turn;
    }
    public static void setColor1(int color) {  // Setter for color1
        color1 = color;
    }
    public static void setColor2(int color) {  // Setter for color2
        color2 = color;
    }
    public static void setNetherRoof(int roof) {  // Setter for netherRoof
        netherRoof = roof;
    }
    public static void setNetherFloor(int floor) {  // Setter for netherFloor
        netherFloor = floor;
    }

    public static void setGameActive(boolean active) {
        gameActive = active;
    }
    public static void setRoundActive(boolean active) {
        roundActive = active;
    }
    public static void setTeam1Ready(boolean ready) {
        team1Ready = ready;
    }
    public static void setTeam2Ready(boolean ready) {
        team2Ready = ready;
    }
    public static void setRoundJustEnded(boolean ended) {
        roundJustEnded = ended;
    }
    public static void setGamePaused(boolean paused) {
        gamePaused = paused;
    }
    public static void setRunnerInFortress(boolean paused) {
        runnerInFortress = paused;
    }

    public static void setPlayer1Name(String ended) {player1Name = ended;}
    public static void setPlayer2Name(String ended) {player2Name = ended;}

    // Getters
    public static int getTeam1Score() {
        return team1Score;
    }
    public static int getTeam2Score() {
        return team2Score;
    }
    public static int getTargetScore() {
        return targetScore;
    }
    public static int getWaitingTimer() {
        return waitingTimer;
    }
    public static int getSpawnTimerR() {
        return spawnTimerR;
    }
    public static int getSpawnTimerH() {
        return spawnTimerH;
    }
    public static int getRound() {
        return round;
    }
    public static int getWhosTurn() {
        return whosTurn;
    }
    public static int getSpawnX() {
        return spawnX;
    }
    public static int getSpawnY() {
        return spawnY;
    }
    public static int getSpawnZ() {
        return spawnZ;
    }
    public static int getColor1() {
        return color1;
    }
    public static int getColor2() {
        return color2;
    }
    public static int getNetherRoof() {
        return netherRoof;
    }
    public static int getNetherFloor() {
        return netherFloor;
    }

    public static boolean isGameActive() {
        return gameActive;
    }
    public static boolean isRoundActive() {
        return roundActive;
    }
    public static boolean isTeam1Ready() {
        return team1Ready;
    }
    public static boolean isTeam2Ready() {
        return team2Ready;
    }
    public static boolean isRoundJustEnded() {
        return roundJustEnded;
    }
    public static boolean isGamePaused() {
        return gamePaused;
    }
    public static boolean isRunnerInFortress() {
        return runnerInFortress;
    }

    public static String getPlayer1Name() {
        return player1Name;
    }
    public static String getPlayer2Name() {
        return player2Name;
    }



    public static int getSpecificTeamColor (int team) {
        if (team == 1) {
            return color1;
        }
        if (team == 2) {
            return color2;
        }
        return -1;
    }
}
