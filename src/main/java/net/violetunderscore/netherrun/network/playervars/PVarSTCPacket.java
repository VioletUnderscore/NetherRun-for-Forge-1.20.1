package net.violetunderscore.netherrun.network.playervars;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKitsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PVarSTCPacket {
    private static final Logger LOGGER = LogManager.getLogger();
    private final boolean canWarp;
    private final boolean warping;
    private final int warpTimer;

    public PVarSTCPacket(boolean canWarp, boolean warping, int warpTimer) {
        this.canWarp = canWarp;
        this.warping = warping;
        this.warpTimer = warpTimer;
    }

    public static void encode(PVarSTCPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.canWarp);
        buf.writeBoolean(msg.warping);
        buf.writeInt(msg.warpTimer);
    }

    public static PVarSTCPacket decode(FriendlyByteBuf buf) {
        return new PVarSTCPacket(
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readInt());
    }

    public static void handle(PVarSTCPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context context = ctxSupplier.get();

        context.enqueueWork(() -> {
            // Ensure this runs only on the client side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg));
        });

        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(PVarSTCPacket msg) {
        Player player = net.minecraft.client.Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                kit.setCanWarp(msg.canWarp);
                kit.setWarping(msg.warping);
                kit.setWarpTimer(msg.warpTimer);
            });
        }
    }
}
