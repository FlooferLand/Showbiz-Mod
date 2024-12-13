package flooferland.showbiz.backend.block.custom;

import flooferland.chirp.util.ChirpDebug;
import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.block.base.EntityTiedBlock;
import flooferland.showbiz.backend.blockEntity.custom.ShowSelectorBlockEntity;
import flooferland.showbiz.backend.entity.custom.InteractPartEntity;
import flooferland.showbiz.backend.networking.InteractableDestroyPayload;
import flooferland.showbiz.backend.util.MultiPartManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.SimpleVoxelShape;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShowSelectorBlock extends EntityTiedBlock {
    public static final DirectionProperty FACING = Properties.FACING;
    
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
    public void onPlaced(World w, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(w, pos, state, placer, itemStack);
        if (!(w instanceof ClientWorld world)) return;  // Spawning client-side
        
        if (world.getBlockEntity(pos) instanceof ShowSelectorBlockEntity blockEntity) {
            blockEntity.multiPart.spawn(world, pos);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {      
        if (!(world instanceof ServerWorld serverWorld)) return;
        
        // No longer the same block (replaced with air / destroyed, or replaced with another block)
        if (!newState.isOf(state.getBlock())) {
            var entities = MultiPartManager.getControlsNear(world, pos).stream().map(Entity::getId).toList();
            for (var player : serverWorld.getPlayers()) {
                ServerPlayNetworking.send(player, new InteractableDestroyPayload(entities));
            }
        }
        
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    /*@Override
    public boolean hasDynamicBounds() {
        return true;
    }

    public VoxelShape getInteractablePartShape() {
        var voxelShape = VoxelShapes.empty();
        if (!multiPart.interactableParts.isEmpty()) {
            for (var entry : multiPart.interactableParts.entrySet()) {
                var name = entry.getKey();
                var part = entry.getValue();
                var shape = VoxelShapes.cuboid(part.size());
                voxelShape = VoxelShapes.combine(voxelShape, shape, (a, b) -> false);
            }
        } else {
            voxelShape = VoxelShapes.fullCube();
        }
        return voxelShape;
    }*/

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
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
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
    // endregion
}
