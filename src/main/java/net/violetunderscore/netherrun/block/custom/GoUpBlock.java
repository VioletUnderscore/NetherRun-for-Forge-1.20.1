package net.violetunderscore.netherrun.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.block.entity.GoUpBlockEntity;
import net.violetunderscore.netherrun.block.entity.ModBlockEntities;
import net.violetunderscore.netherrun.item.ModItems;
import net.violetunderscore.netherrun.network.ItemCooldownPacket;
import net.violetunderscore.netherrun.network.NetworkHandler;
import net.violetunderscore.netherrun.particle.ModParticles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Random;


public class GoUpBlock extends BaseEntityBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final IntegerProperty UP_STRENGTH = IntegerProperty.create("up_strength", 0, 64);
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public GoUpBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UP_STRENGTH);
        builder.add(PLAYER_PLACED);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GoUpBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.GO_UP_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
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
//        if (pEntity instanceof Player) {
//            Player pPlayer = (Player) pEntity;
//            if (pState.getValue(PLAYER_PLACED)) {
//                pPlayer.getCooldowns().addCooldown(ModBlocks.GO_UP.get().asItem(), 15);
//                ItemStack pStack = new ItemStack(ModBlocks.GO_UP.get().asItem(), 1);
//                NetworkHandler.sendToServer(new ItemCooldownPacket(15, pStack, pPlayer.getUUID()));
//            }
//        }
    }

//    @Override
//    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
//
//    }

    //PARTICLE CODE THAT DIDN'T WORK, TO BE RE-USED LATER
}
