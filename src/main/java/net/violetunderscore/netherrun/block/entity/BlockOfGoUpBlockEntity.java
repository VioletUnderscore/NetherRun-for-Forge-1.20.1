package net.violetunderscore.netherrun.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.particle.ModParticles;

import java.util.Random;

public class BlockOfGoUpBlockEntity extends BlockEntity {



    public BlockOfGoUpBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BLOCK_OF_GO_UP_BE.get(), pPos, pBlockState);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.getBlockState(BlockPos.containing(pPos.getX(), pPos.getY() + 1, pPos.getZ())).is(ModBlocks.GO_UP.get())) {
            Random randomPosX = new Random();
            Random randomPosZ = new Random();
            pLevel.addParticle(ModParticles.GO_UP_PARTICLES.get(),
                    pPos.getX() + ((float) (randomPosX.nextInt(60) + 20) / 100), pPos.getY() + 1, pPos.getZ() + ((float) (randomPosZ.nextInt(60) + 20) / 100),
                    0, 0, 0);
        }
    }
}
