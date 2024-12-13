package net.violetunderscore.netherrun.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NetherrunPlaceBlockPacket {
    private final BlockPos pos;
    private final BlockState blockState;

    public NetherrunPlaceBlockPacket(BlockPos pos, BlockState blockState) {
        this.pos = pos;
        this.blockState = blockState;
    }

    public static void encode(NetherrunPlaceBlockPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeVarInt(Block.getId(packet.blockState));
    }

    public static NetherrunPlaceBlockPacket decode(FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        BlockState blockState = Block.stateById(buffer.readVarInt());
        return new NetherrunPlaceBlockPacket(pos, blockState);
    }

    public static void handle(NetherrunPlaceBlockPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(packet));
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(NetherrunPlaceBlockPacket packet) {
        Level level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            level.setBlock(packet.pos, packet.blockState, 3);
        }
    }
}
