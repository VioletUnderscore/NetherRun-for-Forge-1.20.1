package net.violetunderscore.netherrun.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NetherrunTotemPlaceBlockPacket {
    private final BlockPos pos;
    private final BlockState blockState;

    public NetherrunTotemPlaceBlockPacket(BlockPos pos, BlockState blockState) {
        this.pos = pos;
        this.blockState = blockState;
    }

    public static void encode(NetherrunTotemPlaceBlockPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeVarInt(Block.getId(packet.blockState));
    }

    public static NetherrunTotemPlaceBlockPacket decode(FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        BlockState blockState = Block.stateById(buffer.readVarInt());
        return new NetherrunTotemPlaceBlockPacket(pos, blockState);
    }

    public static void handle(NetherrunTotemPlaceBlockPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                Level level = net.minecraft.client.Minecraft.getInstance().level;
                if (level != null) {
                    level.setBlock(packet.pos, packet.blockState, 3);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
