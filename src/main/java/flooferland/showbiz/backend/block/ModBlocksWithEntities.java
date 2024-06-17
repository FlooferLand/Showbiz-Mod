package flooferland.showbiz.backend.block;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.item.base.BlockEntityItem;
import flooferland.showbiz.backend.type.AdvancedBlockData;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.model.GeoModel;

/** Registry class for blocks that have entities attached to them */
public class ModBlocksWithEntities {
    // public static final ModComplexBlock<RefrigeratorBlockEntity> REFRIGERATOR = registerBlockWithEntity(
    //         "refrigerator",
    //         new Refrigerator(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque()),
    //         new Item.Settings().maxCount(1).food(FoodComponents.SWEET_BERRIES), // TODO: FIXME: JOKE
    //         RefrigeratorBlockEntity::new,
    //         new RefrigeratorModel<>()
    // );

    // region Registry functions
    private static <E extends BlockEntity & GeoBlockEntity> AdvancedBlockData<E> registerBlockWithEntity(
            String id,
            BlockWithEntity block,
            Item.Settings itemSettings,
            BlockEntityType.BlockEntityFactory<E> entity,
            GeoModel<GeoAnimatable> model) {
        Identifier identifier = Identifier.of(ShowbizMod.MOD_ID, id);

        // Registering the item
        BlockEntityItem<E> itemType = new BlockEntityItem<>(itemSettings, block, model);
        Item item = Registry.register(Registries.ITEM, identifier, itemType);

        // Registering the block
        Registry.register(Registries.BLOCK, identifier, block);

        // Registering the block entity
        BlockEntityType<E> blockEntity = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                identifier,
                BlockEntityType.Builder.create(entity, block).build()
        );

        // Returning
        return new AdvancedBlockData<>(item, block, blockEntity);
    }
    // endregion

    /** Keeps Java from ignoring the class */
    public static void registerBlocks() {}
}
