package net.violetunderscore.netherrun.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class GoUpParticlePacket {
    private static final Logger LOGGER = LogManager.getLogger();
    private final double x, y, z;
    private final ParticleType<?> particle;

    // Constructor
    public GoUpParticlePacket(double x, double y, double z, ParticleType<?> particle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.particle = particle;
    }

    // Encode the packet data
    public static void encode(GoUpParticlePacket msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeRegistryId(ForgeRegistries.PARTICLE_TYPES, msg.particle);
    }

    // Decode the packet data
    public static GoUpParticlePacket decode(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        ParticleType<?> particle = buf.readRegistryId(); // Adjusted to not take arguments
        return new GoUpParticlePacket(x, y, z, particle);
    }

    // Handle the packet
    public static void handle(GoUpParticlePacket msg, Supplier<NetworkEvent.Context> ctx) {
        //LOGGER.info("NETHERRUN: handle called");
        ctx.get().enqueueWork(() -> {
            Minecraft.getInstance().level.addParticle((ParticleOptions) msg.particle, msg.x, msg.y, msg.z, 0, 0.2, 0);
        });

        ctx.get().setPacketHandled(true);
    }
}