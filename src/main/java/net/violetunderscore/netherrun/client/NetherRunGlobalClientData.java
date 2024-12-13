package net.violetunderscore.netherrun.client;

public class NetherRunGlobalClientData {
    private static int team1Score = 0;
    private static int team2Score = 0;
    private static int targetScore = 0;
    private static int waitingTimer = 0;
    private static int spawnTimerR = 0;
    private static int spawnTimerH = 0;
    private static int round = 0;
    private static int whosTurn = 0;
    private static boolean gameActive = false;
    private static boolean roundActive = false;
    private static boolean team1Ready = false;
    private static boolean team2Ready = false;
    private static boolean roundJustEnded = false;

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
}
