package net.violetunderscore.netherrun.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.violetunderscore.netherrun.item.ModItems;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.network.NetherrunTotemPlaceBlockPacket;

import java.util.Map;
import java.util.Objects;


//sorry this is so hard to read maybe ill come fix it at some point lol :p

public class NetherrunTotemItem extends Item {

    public NetherrunTotemItem(Properties pProperties) {
        super(pProperties);
    }

    /*public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pStack.getTag().getLong("netherrun.enchant_timeout") <= pLevel.getGameTime()
                && pStack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) == 10)
        {
            Map<Enchantment, Integer> pEnchantments = EnchantmentHelper.getEnchantments(pStack);

            pEnchantments.remove(Enchantments.BLOCK_EFFICIENCY);
            EnchantmentHelper.setEnchantments(pEnchantments, pStack);
        }
        else if (pStack.getTag().getLong("netherrun.enchant_timeout") > pLevel.getGameTime()
                && pStack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) != 10)
        {
            pStack.enchant(Enchantments.BLOCK_EFFICIENCY, 10);
        }
        if (pStack.getTag().getLong("netherrun.enchant_ready_timeout") <= pLevel.getGameTime()) {
            pStack.getTag().putBoolean("netherrun.enchant_ready", true);
            pStack.getTag().putInt("CustomModelData", 0);
        }
    }*/

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        /*if (!pLevel.isClientSide() && pPlayer.getMainHandItem().getTag().getLong("netherrun.enchant_ready_timeout") <= pLevel.getGameTime()
                && pPlayer.getMainHandItem().getItem().toString().equals(ModItems.NETHERRUN_PICKAXE.get().toString()))
        {
            pPlayer.getMainHandItem().getTag().putBoolean("netherrun.enchant_ready", false);
            pPlayer.getMainHandItem().getTag().putInt("CustomModelData", 1);
            pPlayer.getMainHandItem().getTag().putLong("netherrun.enchant_timeout", pLevel.getGameTime() + 100);
            pPlayer.getMainHandItem().getTag().putLong("netherrun.enchant_ready_timeout", pLevel.getGameTime() + 600);
        }*/
        if (!pLevel.isClientSide()) {
            if (pLevel instanceof ServerLevel serverLevel) {
                for (int xValue = -1; xValue <= 1; xValue += 1) {
                    for (int zValue = -1; zValue <= 1; zValue += 1) {
                        BlockPos pos = BlockPos.containing(pPlayer.position().x() + xValue, pPlayer.position().y() - 1, pPlayer.position().z() + zValue);
                        pLevel.setBlock(pos, Blocks.CRYING_OBSIDIAN.defaultBlockState(), 3);
                        NetworkHandler.sendToAllPlayers(new NetherrunTotemPlaceBlockPacket(pos, Blocks.CRYING_OBSIDIAN.defaultBlockState()), serverLevel.getServer());
                    }
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        pStack.getOrCreateTag();

        if (pStack.getTag().getLong("netherrun.totem_ready_timeout") <= pLevel.getGameTime()) {
            pStack.getTag().putBoolean("netherrun.totem_ready", true);
            pStack.getTag().putInt("CustomModelData", 0);
        }
        if (pIsSelected == true) {
            if (!pLevel.isClientSide()) {
                if (pLevel instanceof ServerLevel serverLevel) {
                    if (pEntity.isInLava()) {
                        if (pStack.getTag().getBoolean("netherrun.totem_ready")) {
                            pEntity.extinguishFire();
                            for (int yValue = 1; yValue <= 10; yValue += 1) {
                                BlockState state = pLevel.getBlockState(BlockPos.containing(
                                        pEntity.position().x(),
                                        pEntity.position().y() + yValue,
                                        pEntity.position().z()));
                                if (!state.is(Blocks.LAVA)) {
                                    pEntity.teleportTo(
                                            Math.floor(pEntity.position().x()) + 0.5,
                                            Math.floor(pEntity.position().y()) + yValue,
                                            Math.floor(pEntity.position().z()) + 0.5
                                    );
                                    yValue = 11;
                                }
                            }
                            pEntity.fallDistance = 0;
                            BlockState pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                            for (int yValue = -1; yValue <= 3; yValue += 1) {
                                if (yValue == 3) {
                                    pBlockToPlace = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                                }
                                for (int xValue = -1; xValue <= 1; xValue += 1) {
                                    for (int zValue = -1; zValue <= 1; zValue += 1) {
                                        BlockPos pos = BlockPos.containing(
                                                pEntity.position().x() + xValue,
                                                pEntity.position().y() + yValue,
                                                pEntity.position().z() + zValue
                                        );
                                        pLevel.setBlock(pos, pBlockToPlace, 3);
                                        NetworkHandler.sendToAllPlayers(new NetherrunTotemPlaceBlockPacket(pos, pBlockToPlace), serverLevel.getServer());
                                    }
                                }
                                pBlockToPlace = Blocks.AIR.defaultBlockState();
                            }
                            {
                                pStack.getTag().putBoolean("netherrun.totem_ready", false);
                                pStack.getTag().putInt("CustomModelData", 1);
                                pStack.getTag().putLong("netherrun.totem_ready_timeout", pLevel.getGameTime() + 300);
                            }
                        }
                    }
                }
            }
        }
    }
}
