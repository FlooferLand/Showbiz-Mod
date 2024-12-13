package flooferland.showbiz.backend.block.custom;

import flooferland.showbiz.backend.registry.ModBlocks;
import flooferland.showbiz.backend.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class PizzaBlock extends Block {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    protected static final IntProperty SLICES_LEFT = IntProperty.of("slices_left", 1, 4);
    
    public PizzaBlock(Settings settings) {
        super(settings.noCollision().sounds(BlockSoundGroup.WOOL));
        setDefaultState(getDefaultState().with(SLICES_LEFT, SLICES_LEFT.getValues().size()));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SLICES_LEFT);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !(world.getBlockState(pos.down()).getBlock() instanceof PizzaBlock);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.getBlockState(pos.down()) == null) {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ModBlocks.PIZZA.asItem().getDefaultStack()));
            world.removeBlock(pos, false);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        player.getInventory().insertStack(ModItems.PIZZA_SLICE.getDefaultStack());

        // No slices left? Remove the pizza!
        int slicesLeft = state.get(SLICES_LEFT);
        if (slicesLeft == 1) {
            world.removeBlock(pos, false);
        } else {
            world.setBlockState(pos, state.with(SLICES_LEFT, slicesLeft - 1));
        }

        return ActionResult.SUCCESS;
    }
}
