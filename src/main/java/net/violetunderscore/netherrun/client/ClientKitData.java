package net.violetunderscore.netherrun.client;

public class ClientKitData {
    public static String[] playerKit;

    public static void set(String[] kit) {
        ClientKitData.playerKit = kit;
    }

    public static String[] getPlayerKit() {
        return playerKit;
    }
}
