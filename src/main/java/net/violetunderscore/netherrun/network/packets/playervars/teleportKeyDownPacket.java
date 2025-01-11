package net.violetunderscore.netherrun.network.packets.playervars;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKitsProvider;

import java.util.function.Supplier;

public class teleportKeyDownPacket {
    private final boolean isKeyDown;

    public teleportKeyDownPacket(boolean isKeyDown) {
        this.isKeyDown = isKeyDown;
    }

    public static void encode(teleportKeyDownPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isKeyDown);
    }

    public static teleportKeyDownPacket decode(FriendlyByteBuf buf) {
        return new teleportKeyDownPacket(buf.readBoolean());
    }

    public static void handle(teleportKeyDownPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                kit.setKeyDown(msg.isKeyDown);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
