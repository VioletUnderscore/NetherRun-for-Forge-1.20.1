package net.violetunderscore.netherrun.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.violetunderscore.netherrun.client.NetherRunGlobalClientData;

import java.util.function.Supplier;

public class SyncNetherRunScoresPacket {
    private final int team1Score;
    private final int team2Score;
    private final int targetScore;

    public SyncNetherRunScoresPacket(int team1Score, int team2Score, int targetScore) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.targetScore = targetScore;
    }

    public SyncNetherRunScoresPacket(FriendlyByteBuf buf) {
        this.team1Score = buf.readInt();
        this.team2Score = buf.readInt();
        this.targetScore = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(team1Score);
        buf.writeInt(team2Score);
        buf.writeInt(targetScore);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // Store the data in a static client-side cache or client class
            NetherRunGlobalClientData.setScores(team1Score, team2Score, targetScore);
        });
        context.get().setPacketHandled(true);
    }

}
