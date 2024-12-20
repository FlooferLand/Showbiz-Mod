package flooferland.showbiz.backend.block.base;

import flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import flooferland.showbiz.backend.util.ShowbizUtil;
import flooferland.showbiz.client.registry.ModScreenHandlers;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/** Simplifies the creation of containers, and lets them share functionality */
public class ContainerBlock extends EntityTiedBlock {
    public ContainerBlock(Settings settings, ICreateBlockEntity blockEntity) {
        super(settings, blockEntity);
        CODEC = ContainerBlock.createCodec((codecSettings) -> new ContainerBlock(codecSettings, BLOCK_ENTITY_MAKER));
    }

    @Nullable
    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return (NamedScreenHandlerFactory) ModScreenHandlers.CONTAINER_BLOCK_SCREEN_HANDLER;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.PASS;
        
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ReelHolderBlockEntity reelHolder) {
            player.openHandledScreen(reelHolder);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return super.getComparatorOutput(state, world, pos);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ShowbizUtil.scatterInventoryOnDestroy(state, world, pos, newState, moved);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World _world, BlockState _state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlocksWithEntities.REEL_HOLDER.entity(),
                (world, pos, state, blockEntity) -> blockEntity.tick(world, pos, state));
    }
}

