package net.violetunderscore.netherrun.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.PacketDistributor;
import net.violetunderscore.netherrun.network.playervars.PVarSTCPacket;
import net.violetunderscore.netherrun.network.playervars.teleportKeyDownPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("netherrun", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, NetherrunPlaceBlockPacket.class, NetherrunPlaceBlockPacket::encode, NetherrunPlaceBlockPacket::decode, NetherrunPlaceBlockPacket::handle);
        INSTANCE.registerMessage(id++, GoUpParticlePacket.class, GoUpParticlePacket::encode, GoUpParticlePacket::decode, GoUpParticlePacket::handle);
        INSTANCE.registerMessage(id++, ItemCooldownPacket.class, ItemCooldownPacket::encode, ItemCooldownPacket::decode, ItemCooldownPacket::handle);
        INSTANCE.registerMessage(id++, SyncNetherRunScoresPacket.class, SyncNetherRunScoresPacket::encode, SyncNetherRunScoresPacket::new, SyncNetherRunScoresPacket::handle);
        INSTANCE.registerMessage(id++, teleportKeyDownPacket.class, teleportKeyDownPacket::encode, teleportKeyDownPacket::decode, teleportKeyDownPacket::handle);
        INSTANCE.registerMessage(id++, PVarSTCPacket.class, PVarSTCPacket::encode, PVarSTCPacket::decode, PVarSTCPacket::handle);
    }

    public static void sendToAllPlayers(NetherrunPlaceBlockPacket packet, MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }
    public static void sendToNear(GoUpParticlePacket packet, MinecraftServer server, double rad) {
        //LOGGER.info("NETHERRUN: sendToNear called");
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            //LOGGER.info("NETHERRUN: sendToNear for loop working");
            INSTANCE.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(player.getX(), player.getY(), player.getZ(), rad, player.level().dimension())), packet);
            //INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }

    public static void sendToServer(ItemCooldownPacket packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void sendTargeted(PVarSTCPacket packet, ServerPlayer sPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> sPlayer), packet);
    }
    public static void sendTargeted(NetherrunPlaceBlockPacket packet, ServerPlayer sPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> sPlayer), packet);
    }
}
