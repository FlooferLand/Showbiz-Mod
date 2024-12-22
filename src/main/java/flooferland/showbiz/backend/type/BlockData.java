package flooferland.showbiz.backend.type;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public record BlockData<TBlock extends Block, TItem extends BlockItem>(TBlock block, TItem item, Identifier id) { }
