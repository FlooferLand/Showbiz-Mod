package flooferland.showbiz.backend.block.base;

import com.mojang.serialization.MapCodec;
import flooferland.showbiz.backend.blockEntity.ModBlocksWithEntities;
import flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import flooferland.showbiz.backend.util.ShowbizUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/** Simplifies the creation of containers, and lets them share functionality */
public class ContainerBlock extends BlockWithEntity {
    public static MapCodec<ContainerBlock> CODEC = null;
    public static ICreateBlockEntity BLOCK_ENTITY_MAKER = null;

    public interface ICreateBlockEntity {
        LootableContainerBlockEntity createBlockEntity(BlockPos pos, BlockState state);
    }

    public ContainerBlock(Settings settings, ICreateBlockEntity blockEntity) {
        super(settings);
        BLOCK_ENTITY_MAKER = blockEntity;
        CODEC = ContainerBlock.createCodec((codecSettings) -> new ContainerBlock(codecSettings, BLOCK_ENTITY_MAKER));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BLOCK_ENTITY_MAKER.createBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // Object interaction
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ReelHolderBlockEntity) {
            player.openHandledScreen((ReelHolderBlockEntity) blockEntity);
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
        return validateTicker(type, ModBlocksWithEntities.REEL_HOLDER.entity,
                (world, pos, state, blockEntity) -> blockEntity.tick(world, pos, state));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
}

