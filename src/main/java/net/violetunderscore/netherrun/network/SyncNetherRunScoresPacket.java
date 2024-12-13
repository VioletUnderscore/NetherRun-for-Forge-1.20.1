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
    private final boolean gameActive;
    private final boolean roundActive;
    private final boolean team1Ready;
    private final boolean team2Ready;
    private final boolean roundJustEnded;

    public SyncNetherRunScoresPacket(int team1Score, int team2Score, int targetScore, int waitingTimer, int spawnTimerR, int spawnTimerH, int round, int whosTurn, boolean gameActive, boolean roundActive, boolean team1Ready, boolean team2Ready, boolean roundJustEnded) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.targetScore = targetScore;
        this.waitingTimer = waitingTimer;
        this.spawnTimerR = spawnTimerR;
        this.spawnTimerH = spawnTimerH;
        this.round = round;
        this.whosTurn = whosTurn;
        this.gameActive = gameActive;
        this.roundActive = roundActive;
        this.team1Ready = team1Ready;
        this.team2Ready = team2Ready;
        this.roundJustEnded = roundJustEnded;
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
        this.gameActive = buf.readBoolean();
        this.roundActive = buf.readBoolean();
        this.team1Ready = buf.readBoolean();
        this.team2Ready = buf.readBoolean();
        this.roundJustEnded = buf.readBoolean();
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
        buf.writeBoolean(gameActive);
        buf.writeBoolean(roundActive);
        buf.writeBoolean(team1Ready);
        buf.writeBoolean(team2Ready);
        buf.writeBoolean(roundJustEnded);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // Store the data in a static client-side cache or client class
            NetherRunGlobalClientData.setScore1(team1Score);
            NetherRunGlobalClientData.setScore2(team2Score);
            NetherRunGlobalClientData.setScoreTarget(targetScore);
            NetherRunGlobalClientData.setWaitingTimer(waitingTimer);
            NetherRunGlobalClientData.setSpawnTimerR(spawnTimerR);
            NetherRunGlobalClientData.setSpawnTimerH(spawnTimerH);
            NetherRunGlobalClientData.setRound(round);
            NetherRunGlobalClientData.setWhosTurn(whosTurn);
            NetherRunGlobalClientData.setGameActive(gameActive);
            NetherRunGlobalClientData.setRoundActive(roundActive);
            NetherRunGlobalClientData.setTeam1Ready(team1Ready);
            NetherRunGlobalClientData.setTeam2Ready(team2Ready);
            NetherRunGlobalClientData.setRoundJustEnded(roundJustEnded);
        });
        context.get().setPacketHandled(true);
    }
}
