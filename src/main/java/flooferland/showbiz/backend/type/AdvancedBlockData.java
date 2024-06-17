package flooferland.showbiz.backend.type;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;

public class AdvancedBlockData<E extends BlockEntity> {
    public final Item item;
    public final Block block;
    public final BlockEntityType<E> entity;

    public AdvancedBlockData(Item item, Block block, BlockEntityType<E> entity) {
        this.item = item;
        this.block = block;
        this.entity = entity;
    }
}
