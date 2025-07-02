package com.flooferland.showbiz.backend.block.custom;

import com.flooferland.showbiz.backend.block.base.EntityTiedBlock;
import com.flooferland.showbiz.backend.blockEntity.custom.ShowSelectorBlockEntity;
import com.flooferland.showbiz.backend.networking.InteractableDestroyPayload;
import com.flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import com.flooferland.showbiz.backend.util.MultiPartManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShowSelectorBlock extends EntityTiedBlock {
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final boolean ALWAYS_SHOW_INTERACTION_LABELS = false;
    
    public ShowSelectorBlock(Settings settings) {
        super(settings, ShowSelectorBlockEntity::new);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }
    
    private VoxelShape getShape(BlockState state, BlockView world, BlockPos pos) {
        var girth = 0.43;
        return VoxelShapes.cuboid(new Box(girth, 0, girth, 1 - girth, 0.7, 1 - girth));
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) { return getShape(state, world, pos); }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) { return getShape(state, world, pos); }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        // No longer the same block (replaced with air / destroyed, or replaced with another block)
        if (!state.isOf(state.getBlock())) {
            var entities = MultiPartManager.getControlsNear(world, pos).stream().map(Entity::getId).toList();
            for (var player : world.getPlayers()) {
                ServerPlayNetworking.send(player, new InteractableDestroyPayload(entities));
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlocksWithEntities.SHOW_SELECTOR.entity(),
            (world1, pos1, state1, blockEntity) -> blockEntity.tick(world1, pos1, state1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    // region | Facing stuff
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected boolean isTransparent(BlockState state) {
        return true;
    }
    // endregion
}
