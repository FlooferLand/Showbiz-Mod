package flooferland.showbiz.backend.block;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.block.custom.Pizza;
import flooferland.showbiz.backend.block.custom.ReelHolder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/** Registry class for blocks */
public class ModBlocks {
    // region | Blocks
    public static final Block PIZZA = registerBlock(
            "pizza",
            new Pizza(AbstractBlock.Settings.copy(Blocks.YELLOW_CARPET)),
            new Item.Settings().maxCount(8)
    );
    // endregion
    
    // region | Block registry functions
    private static Block registerBlock(String name, Block block) {
        return registerBlock(name, block, new Item.Settings());
    }
    private static Block registerBlock(String name, Block block, Item.Settings settings) {
        Identifier identifier = Identifier.of(ShowbizMod.MOD_ID, name);
        registerBlockItem(identifier, block, settings);
        return Registry.register(Registries.BLOCK, identifier, block);
    }

    private static void registerBlockItem(Identifier id, Block block, Item.Settings settings) {
        Registry.register(Registries.ITEM, id, new BlockItem(block, settings));
    }

    /** Keeps Java from ignoring the class */
    public static void registerBlocks() {
        //ModBlockFamilies.registerBlockFamilies();
    }
    // endregion
}
