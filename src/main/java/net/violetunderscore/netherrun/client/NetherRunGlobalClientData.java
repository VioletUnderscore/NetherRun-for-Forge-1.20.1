package net.violetunderscore.netherrun.client;

public class NetherRunGlobalClientData {
    private static int team1Score = 0;
    private static int team2Score = 0;
    private static int targetScore = 0;

    public static void setScores(int t1, int t2, int target) {
        team1Score = t1;
        team2Score = t2;
        targetScore = target;
    }

    public static int getTeam1Score() {
        return team1Score;
    }

    public static int getTeam2Score() {
        return team2Score;
    }

    public static int getTargetScore() {
        return targetScore;
    }
}
