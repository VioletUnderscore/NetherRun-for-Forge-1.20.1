package net.violetunderscore.netherrun.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.violetunderscore.netherrun.client.NetherRunGlobalClientData;

import java.util.function.Supplier;

public class SyncNetherRunScoresPacket {
    private final int team1Score;
    private final int team2Score;
    private final int targetScore;
    private final int waitingTimer;
    private final int spawnTimerR;
    private final int spawnTimerH;
    private final int round;
    private final int whosTurn;
    private final int spawnX;
    private final int spawnY;
    private final int spawnZ;
    private final int color1;
    private final int color2;
    private final int netherRoof;
    private final int netherFloor;
    private final boolean gameActive;
    private final boolean roundActive;
    private final boolean team1Ready;
    private final boolean team2Ready;
    private final boolean roundJustEnded;
    private final String player1Name;
    private final String player2Name;

    public SyncNetherRunScoresPacket(int team1Score, int team2Score, int targetScore, int waitingTimer,
                                     int spawnTimerR, int spawnTimerH, int round, int whosTurn,
                                     int spawnX, int spawnY, int spawnZ,
                                     int color1, int color2, int netherRoof, int netherFloor,
                                     boolean gameActive, boolean roundActive,
                                     boolean team1Ready, boolean team2Ready, boolean roundJustEnded,
                                     String player1Name, String player2Name) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.targetScore = targetScore;
        this.waitingTimer = waitingTimer;
        this.spawnTimerR = spawnTimerR;
        this.spawnTimerH = spawnTimerH;
        this.round = round;
        this.whosTurn = whosTurn;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        this.color1 = color1;
        this.color2 = color2;
        this.netherRoof = netherRoof;
        this.netherFloor = netherFloor;
        this.gameActive = gameActive;
        this.roundActive = roundActive;
        this.team1Ready = team1Ready;
        this.team2Ready = team2Ready;
        this.roundJustEnded = roundJustEnded;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public SyncNetherRunScoresPacket(FriendlyByteBuf buf) {
        this.team1Score = buf.readInt();
        this.team2Score = buf.readInt();
        this.targetScore = buf.readInt();
        this.waitingTimer = buf.readInt();
        this.spawnTimerR = buf.readInt();
        this.spawnTimerH = buf.readInt();
        this.round = buf.readInt();
        this.whosTurn = buf.readInt();
        this.spawnX = buf.readInt();
        this.spawnY = buf.readInt();
        this.spawnZ = buf.readInt();
        this.color1 = buf.readInt();
        this.color2 = buf.readInt();
        this.netherRoof = buf.readInt();
        this.netherFloor = buf.readInt();
        this.gameActive = buf.readBoolean();
        this.roundActive = buf.readBoolean();
        this.team1Ready = buf.readBoolean();
        this.team2Ready = buf.readBoolean();
        this.roundJustEnded = buf.readBoolean();
        this.player1Name = buf.readUtf(32767);
        this.player2Name = buf.readUtf(32767);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(team1Score);
        buf.writeInt(team2Score);
        buf.writeInt(targetScore);
        buf.writeInt(waitingTimer);
        buf.writeInt(spawnTimerR);
        buf.writeInt(spawnTimerH);
        buf.writeInt(round);
        buf.writeInt(whosTurn);
        buf.writeInt(spawnX);
        buf.writeInt(spawnY);
        buf.writeInt(spawnZ);
        buf.writeInt(color1);
        buf.writeInt(color2);
        buf.writeInt(netherRoof);
        buf.writeInt(netherFloor);
        buf.writeBoolean(gameActive);
        buf.writeBoolean(roundActive);
        buf.writeBoolean(team1Ready);
        buf.writeBoolean(team2Ready);
        buf.writeBoolean(roundJustEnded);
        buf.writeUtf(player1Name);
        buf.writeUtf(player2Name);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            NetherRunGlobalClientData.setScore1(team1Score);
            NetherRunGlobalClientData.setScore2(team2Score);
            NetherRunGlobalClientData.setScoreTarget(targetScore);
            NetherRunGlobalClientData.setWaitingTimer(waitingTimer);
            NetherRunGlobalClientData.setSpawnTimerR(spawnTimerR);
            NetherRunGlobalClientData.setSpawnTimerH(spawnTimerH);
            NetherRunGlobalClientData.setRound(round);
            NetherRunGlobalClientData.setWhosTurn(whosTurn);
            NetherRunGlobalClientData.setSpawnX(spawnX);
            NetherRunGlobalClientData.setSpawnY(spawnY);
            NetherRunGlobalClientData.setSpawnZ(spawnZ);
            NetherRunGlobalClientData.setColor1(color1);
            NetherRunGlobalClientData.setColor2(color2);
            NetherRunGlobalClientData.setNetherRoof(netherRoof);
            NetherRunGlobalClientData.setNetherFloor(netherFloor);
            NetherRunGlobalClientData.setGameActive(gameActive);
            NetherRunGlobalClientData.setRoundActive(roundActive);
            NetherRunGlobalClientData.setTeam1Ready(team1Ready);
            NetherRunGlobalClientData.setTeam2Ready(team2Ready);
            NetherRunGlobalClientData.setRoundJustEnded(roundJustEnded);
            NetherRunGlobalClientData.setPlayer1Name(player1Name);
            NetherRunGlobalClientData.setPlayer2Name(player2Name);
        });
        context.get().setPacketHandled(true);
    }
}