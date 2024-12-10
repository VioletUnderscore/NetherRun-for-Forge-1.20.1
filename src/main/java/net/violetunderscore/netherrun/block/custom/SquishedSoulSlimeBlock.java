package net.violetunderscore.netherrun.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SquishedSoulSlimeBlock extends SoulSlimeBlock {

//    private static final VoxelShape SOUL_SLIME_SHAPE;
//
//    static {
//        VoxelShape shape = Shapes.empty();
//        for(int i = 0; i < 16; i++) {
//            shape = Shapes.or(shape, Block.box(0, i, 0, 16, i + 1, 16));
//        }
//        SOUL_SLIME_SHAPE = shape;
//    }

    private static final VoxelShape SOUL_SLIME_SHAPE = Block.box(0, 0, 0, 16, 4, 16);


    public SquishedSoulSlimeBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SOUL_SLIME_SHAPE;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SOUL_SLIME_SHAPE;
    }
}
