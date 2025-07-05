package com.flooferland.showbiz.backend.block.base;

import com.flooferland.showbiz.backend.blockEntity.base.ContainerBlockEntity;
import com.flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import com.flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import com.flooferland.showbiz.backend.util.ShowbizUtil;
import com.flooferland.showbiz.client.registry.ModScreenHandlers;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
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
        if (blockEntity instanceof ContainerBlockEntity entity) {
            player.openHandledScreen(entity);
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
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        ItemScatterer.onStateReplaced(state, world, pos);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World _world, BlockState _state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlocksWithEntities.REEL_HOLDER.entity(),
                (world, pos, state, blockEntity) -> blockEntity.tick(world, pos, state));
    }
}

