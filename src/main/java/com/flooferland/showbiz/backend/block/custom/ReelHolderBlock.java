package com.flooferland.showbiz.backend.block.custom;

import com.flooferland.showbiz.backend.block.base.ContainerBlock;
import com.flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ReelHolderBlock extends ContainerBlock {
    public static final int maxReelCount = 7;
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    
    public ReelHolderBlock(Settings settings) {
        super(settings, ReelHolderBlockEntity::new);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

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

    // region | Voxel shapes
    protected VoxelShape getVoxelShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.0067187499999999956, 0.002528125, 0.35109375, 0.99328125, 0.002528125, 0.77390625),
                VoxelShapes.cuboid(0.99328125, 0.002528125, 0.35109375, 0.99328125, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.0067187499999999956, 0.002528125, 0.77390625, 0.99328125, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.0067187499999999956, 0.002528125, 0.35109375, 0.0067187499999999956, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.14765625, 0.002528125, 0.35109375, 0.14765625, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.28859375, 0.002528125, 0.35109375, 0.28859375, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.42953125000000003, 0.002528125, 0.35109375, 0.42953125000000003, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.57046875, 0.002528125, 0.35109375, 0.57046875, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.71140625, 0.002528125, 0.35109375, 0.71140625, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.85234375, 0.002528125, 0.35109375, 0.85234375, 0.425340625, 0.77390625),
                VoxelShapes.cuboid(0.0067187499999999956, 0.002528125, 0.35109375, 0.99328125, 0.143465625, 0.35109375),
                VoxelShapes.cuboid(0.887578125, 0.030485625, 0.296895625, 0.958046875, 0.382829375, 0.6492393750000001),
                VoxelShapes.cuboid(0.746640625, 0.030485625, 0.296895625, 0.817109375, 0.382829375, 0.6492393750000001),
                VoxelShapes.cuboid(0.605703125, 0.030485625, 0.296895625, 0.676171875, 0.382829375, 0.6492393750000001),
                VoxelShapes.cuboid(0.464765625, 0.030485625, 0.296895625, 0.535234375, 0.382829375, 0.6492393750000001),
                VoxelShapes.cuboid(0.323828125, 0.030485625, 0.296895625, 0.394296875, 0.382829375, 0.6492393750000001),
                VoxelShapes.cuboid(0.04195312499999998, 0.030485625, 0.296895625, 0.112421875, 0.382829375, 0.6492393750000001),
                VoxelShapes.cuboid(0.18289062499999997, 0.030485625, 0.296895625, 0.253359375, 0.382829375, 0.6492393750000001)
        ).asCuboid();
    }
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getVoxelShape();
    }
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getVoxelShape();
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return  getVoxelShape();
    }
    // endregion

    // TODO: Rewrite this code to go with a more chiseled bookshelf interaction approach for reel holders
    /*@Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand != Hand.MAIN_HAND || world.isClient) return ItemActionResult.CONSUME;
        
        final ItemStack mainHandStack = player.getMainHandStack();
        
        // Return if the player is holding an item that isn't a reel
        if (!mainHandStack.isEmpty() && !mainHandStack.isOf(ModItems.REEL)) {
            return ItemActionResult.CONSUME;
        }
        
        // Getting the block entity
        if (!(world.getBlockEntity(pos) instanceof ReelHolderBlockEntity blockEntity)) {
            return ItemActionResult.CONSUME;
        }
        
        // Getting the position where the player interacted
        Optional<Vec2f> hitPos = ShowbizUtil.getHitPos(hit, state.get(FACING));
        if (hitPos.isEmpty()) {
            return ItemActionResult.CONSUME;
        }
        float pad = 0.05F;
        int hitIndex = 0;
        for (float bound = 0; bound < 1.0F - pad; bound += (1.0F / maxReelCount)) {
            float hitLoc = hitPos.get().x - pad;
            if (hitLoc > bound - pad && hitLoc < bound + pad) {
                break;
            }
            hitIndex++;
        }
        hitIndex = Math.clamp(hitIndex, 0, maxReelCount-1);

        // Adding / removing reels
        {
            ItemStack holderReel = blockEntity.inventory.getStack(hitIndex);
            
            // Giving the player a reel from the holder
            if (mainHandStack.isEmpty() && !holderReel.isEmpty()) {
                player.setStackInHand(Hand.MAIN_HAND, holderReel);
                blockEntity.inventory.removeStack(hitIndex);
            }

            // Adding a reel to the holder
            else if (!mainHandStack.isEmpty() && holderReel.isEmpty()) {
                blockEntity.inventory.setStack(hitIndex, mainHandStack);
                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            }
                
            // Swapping reels when both stacks are full
            else if (!mainHandStack.isEmpty() && !holderReel.isEmpty()) {
                player.setStackInHand(Hand.MAIN_HAND, holderReel);
                blockEntity.inventory.setStack(hitIndex, mainHandStack);
            }
        }

        return ItemActionResult.SUCCESS;
    }*/
}
