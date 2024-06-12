package net.violetunderscore.netherrun.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.block.custom.GoUpBlock;

import javax.annotation.Nonnull;

import static net.violetunderscore.netherrun.block.custom.GoUpBlock.UP_STRENGTH;

public class GoUpBlockEntity extends BlockEntity {
    private int upstrength;

    public GoUpBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GO_UP_BE.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.upstrength = pTag.getInt("netherrun.upstrength");
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("netherrun.upstrength", this.upstrength);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        //CLIENT
        if(pLevel.isClientSide()) {

        }
        //SERVER
        else if(!pLevel.isClientSide()) {

        }
        //BOTH
        BlockPos below1 = BlockPos.containing(pPos.getX(), pPos.getY() - 1, pPos.getZ());
        BlockPos abovePos = BlockPos.containing(pPos.getX(), pPos.getY() + 1, pPos.getZ());
        if ((!(pLevel.getBlockState(below1).is(ModBlocks.BLOCK_OF_GO_UP.get()))
                && !(pLevel.getBlockState(below1).is(ModBlocks.GO_UP.get())))
                || (pLevel.getBlockState(below1).is(ModBlocks.GO_UP.get())
                && pLevel.getBlockState(below1).getValue(UP_STRENGTH) <= 1)) {
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 0);
        }
        int currentStrength = pState.getValue(UP_STRENGTH);

        if (currentStrength > 1) {
            if (pLevel.getBlockState(abovePos).isAir()) {
                pLevel.setBlock(abovePos, ModBlocks.GO_UP.get().defaultBlockState().setValue(UP_STRENGTH, currentStrength - 1), 3);
            } else if (pLevel.getBlockState(abovePos).is(ModBlocks.GO_UP.get()) && pLevel.getBlockState(abovePos).getValue(UP_STRENGTH) != currentStrength - 1) {
                pLevel.setBlock(abovePos, ModBlocks.GO_UP.get().defaultBlockState().setValue(UP_STRENGTH, currentStrength - 1), 3);
            }
        }
    }
}
