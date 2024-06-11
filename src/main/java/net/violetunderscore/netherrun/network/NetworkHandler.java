package net.violetunderscore.netherrun.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.PacketDistributor;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("netherrun", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, NetherrunTotemPlaceBlockPacket.class, NetherrunTotemPlaceBlockPacket::encode, NetherrunTotemPlaceBlockPacket::decode, NetherrunTotemPlaceBlockPacket::handle);
    }

    public static void sendToAllPlayers(NetherrunTotemPlaceBlockPacket packet, MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }
}
