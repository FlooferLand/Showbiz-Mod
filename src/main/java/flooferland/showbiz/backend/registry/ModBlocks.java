package flooferland.showbiz.backend.registry;

import flooferland.showbiz.backend.block.custom.PizzaBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;

/** Registry class for blocks */
public class ModBlocks {
    // region | Blocks
    public static final PizzaBlock PIZZA = ShowbizRegistry.block(
            "pizza",
            (settings) -> new PizzaBlock(settings.copy(Blocks.YELLOW_CARPET)),
            (block, settings, __) -> new BlockItem(block, settings.maxCount(8))
    );
    // endregion
    
    /** Keeps Java from ignoring the class */
    public static void registerBlocks() {
        //ModBlockFamilies.blockFamilies();
    }
}
