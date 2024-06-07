package net.violetunderscore.netherrun.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.violetunderscore.netherrun.block.ModBlocks;

public class GoUpBlockEntity extends BlockEntity {



    public GoUpBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GO_UP_BE.get(), pPos, pBlockState);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        //CLIENT
        if(pLevel.isClientSide()) {

        }
        //SERVER
        else if(!pLevel.isClientSide()) {

        }
        //BOTH
        if (!(pLevel.getBlockState(BlockPos.containing(pPos.getX(), pPos.getY() - 1, pPos.getZ())).is(ModBlocks.BLOCK_OF_GO_UP.get())
                || pLevel.getBlockState(BlockPos.containing(pPos.getX(), pPos.getY() - 1, pPos.getZ())).is(ModBlocks.GO_UP.get()))) {
            pLevel.setBlock(BlockPos.containing(pPos.getX(), pPos.getY(), pPos.getZ()), Blocks.AIR.defaultBlockState(), 0);
        }
    }
}
