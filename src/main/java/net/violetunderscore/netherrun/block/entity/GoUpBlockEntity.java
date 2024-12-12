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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static net.violetunderscore.netherrun.block.custom.GoUpBlock.*;

public class GoUpBlockEntity extends BlockEntity {

    private static final Logger LOGGER = LogManager.getLogger();
    private int upstrength;
    private int lifetime;

    public GoUpBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GO_UP_BE.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.upstrength = pTag.getInt("netherrun.upstrength");
        this.lifetime = pTag.getInt("netherrun.lifetime");
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("netherrun.upstrength", this.upstrength);
        pTag.putInt("netherrun.lifetime", this.lifetime);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        //CLIENT
        if (pLevel.isClientSide()) {
            // Client-side behavior (if needed)
        }
        //SERVER
        else if (!pLevel.isClientSide()) {
            // Server-side behavior
            //LOGGER.info("L: " + pState.getValue(LIFETIME) + ", S: " + pState.getValue(UP_STRENGTH) + ", P: " +  pState.getValue(PLAYER_PLACED));

            if (pState.getValue(PLAYER_PLACED)) {

                int currentLifetime = pState.getValue(LIFETIME);

                if (currentLifetime <= 0) {
                    pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
                } else {
                    // Update the block state with the decreased lifetime
                    pLevel.setBlock(pPos, pState.setValue(LIFETIME, currentLifetime - 1), 3);
                }
            } else {
                // Original non-player-placed block behavior
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
                        pLevel.setBlock(abovePos, ModBlocks.GO_UP.get().defaultBlockState().setValue(UP_STRENGTH, currentStrength - 1).setValue(PLAYER_PLACED, false), 3);
                    } else if (pLevel.getBlockState(abovePos).is(ModBlocks.GO_UP.get()) && pLevel.getBlockState(abovePos).getValue(UP_STRENGTH) != currentStrength - 1) {
                        pLevel.setBlock(abovePos, ModBlocks.GO_UP.get().defaultBlockState().setValue(UP_STRENGTH, currentStrength - 1).setValue(PLAYER_PLACED, false), 3);
                    }
                }
            }
        }
    }
}