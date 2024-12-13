package flooferland.showbiz.backend.type;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public record AdvancedBlockData<E extends BlockEntity>(Item item, Block block, BlockEntityType<E> entity, Identifier id) { }
