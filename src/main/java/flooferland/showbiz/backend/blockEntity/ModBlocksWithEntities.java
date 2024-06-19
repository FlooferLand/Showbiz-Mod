package flooferland.showbiz.backend.blockEntity;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.block.custom.ReelHolder;
import flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import flooferland.showbiz.backend.item.base.BlockEntityItem;
import flooferland.showbiz.backend.type.AdvancedBlockData;
import flooferland.showbiz.client.blockEntity.model.ReelHolderModel;
import flooferland.showbiz.client.item.base.BlockEntityItemRenderer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.model.GeoModel;

/** Registry class for blocks that have entities attached to them */
public class ModBlocksWithEntities {
    public static final AdvancedBlockData<ReelHolderBlockEntity> REEL_HOLDER = registerBlockWithEntity(
            "reel_holder",
            new ReelHolder(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque()),
            new Item.Settings().maxCount(1).food(FoodComponents.SWEET_BERRIES), // TODO: FIXME: JOKE
            ReelHolderBlockEntity::new,
            new ReelHolderModel<>()
    );

    // region Registry functions
    @SuppressWarnings("SameParameterValue")
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
