package net.violetunderscore.netherrun.network;


import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.function.Supplier;

public class ItemCooldownPacket {
    private static final Logger LOGGER = LogManager.getLogger();
    private final int pCooldown;
    private final ItemStack pItem;
    private final UUID pUUID;

    public ItemCooldownPacket (int pCooldown, ItemStack pItem, UUID pUUID) {
        this.pCooldown = pCooldown;
        this.pItem = pItem;
        this.pUUID = pUUID;
    }

    public static void encode(ItemCooldownPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.pCooldown);
        buf.writeItem(msg.pItem);
        buf.writeUUID(msg.pUUID);
    }

    // Decode the packet data
    public static ItemCooldownPacket decode(FriendlyByteBuf buf) {
        int pCooldown = buf.readInt();
        ItemStack pItem = buf.readItem();
        UUID pUUID = buf.readUUID();
        return new ItemCooldownPacket(pCooldown, pItem, pUUID);
    }


    // Handle the packet
    public static void handle(ItemCooldownPacket msg, Supplier<NetworkEvent.Context> ctx) {
        //LOGGER.info("NETHERRUN: handle called");
        ctx.get().enqueueWork(() -> {
            MinecraftServer pServer = ctx.get().getSender().getServer();
            ServerPlayer pServerPlayer = pServer.getPlayerList().getPlayer(msg.pUUID);
            if (pServerPlayer != null) {
                Minecraft.getInstance().player.getCooldowns().addCooldown(msg.pItem.getItem(), msg.pCooldown);
            }
        });

        ctx.get().setPacketHandled(true);
    }

}
