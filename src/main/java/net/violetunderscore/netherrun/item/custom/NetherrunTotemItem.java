package net.violetunderscore.netherrun.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.network.NetherrunPlaceBlockPacket;


//sorry this is so hard to read maybe ill come fix it at some point lol :p

public class NetherrunTotemItem extends Item {

    public NetherrunTotemItem(Properties pProperties) {
        super(pProperties);
    }

    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

        if (pStack.getOrCreateTag().getLong("netherrun.ready_timeout") <= pLevel.getGameTime()) {
            pStack.getOrCreateTag().putBoolean("netherrun.ready", true);
            pStack.getOrCreateTag().putInt("CustomModelData", 0);
        }
        if (pIsSelected) {
            if (!pLevel.isClientSide()) {
                if (pLevel instanceof ServerLevel serverLevel) {
                    if (pEntity.isInLava()) {
                        if (!(pEntity instanceof Player _player && (_player.isSpectator() || _player.isCreative()) && !_player.isDeadOrDying())) {
                            if (pStack.getOrCreateTag().getBoolean("netherrun.ready")) {
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
                                            NetworkHandler.sendToAllPlayers(new NetherrunPlaceBlockPacket(pos, pBlockToPlace), serverLevel.getServer());
                                        }
                                    }
                                    pBlockToPlace = Blocks.AIR.defaultBlockState();
                                }
                                {
                                    pStack.getOrCreateTag().putBoolean("netherrun.ready", false);
                                    pStack.getOrCreateTag().putInt("CustomModelData", 1);
                                    pStack.getOrCreateTag().putLong("netherrun.ready_timeout", pLevel.getGameTime() + 300);
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
