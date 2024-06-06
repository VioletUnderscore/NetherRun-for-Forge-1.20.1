package net.violetunderscore.netherrun.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;



public class GoUpBlock extends Block {
    public GoUpBlock(Properties pProperties) {
        super(pProperties);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }


    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        pEntity.fallDistance = 0;
        if (!pEntity.isShiftKeyDown()) {
            pEntity.setDeltaMovement(new Vec3((pEntity.getDeltaMovement().x()), Math.max(pEntity.getDeltaMovement().y, 0.5), (pEntity.getDeltaMovement().z())));
        }
        else {
            pEntity.setDeltaMovement(new Vec3((pEntity.getDeltaMovement().x()), Math.max(pEntity.getDeltaMovement().y, -0.2), (pEntity.getDeltaMovement().z())));
        }
    }
}
