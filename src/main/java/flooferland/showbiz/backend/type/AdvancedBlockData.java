package flooferland.showbiz.backend.type;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;

public record AdvancedBlockData<E extends BlockEntity>(Item item, Block block, BlockEntityType<E> entity) { }
