package flooferland.showbiz.backend.registry;

import flooferland.showbiz.backend.block.custom.ReelHolderBlock;
import flooferland.showbiz.backend.block.custom.ShowSelectorBlock;
import flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import flooferland.showbiz.backend.blockEntity.custom.ShowSelectorBlockEntity;
import flooferland.showbiz.backend.type.BlockEntityData;
import flooferland.showbiz.client.blockEntity.model.ReelHolderModel;
import flooferland.showbiz.client.blockEntity.model.ShowSelectorModel;
import flooferland.showbiz.client.blockEntity.renderer.ReelHolderRenderer;
import flooferland.showbiz.client.blockEntity.renderer.ShowSelectorRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.BlockItem;

/** Registry class for blocks that have entities attached to them */
public class ModBlocksWithEntities {
    public static final BlockEntityData<ReelHolderBlockEntity> REEL_HOLDER = ShowbizRegistry.blockWithEntity(
            "reel_holder",
            (settings) -> new ReelHolderBlock(settings.copy(Blocks.IRON_BLOCK).nonOpaque()),
            new BlockItem.Settings().maxCount(1),
            ReelHolderBlockEntity::new,
            new ReelHolderModel<>()
    );
    public static final BlockEntityData<ShowSelectorBlockEntity> SHOW_SELECTOR = ShowbizRegistry.blockWithEntity(
            "show_selector",
            (settings) -> new ShowSelectorBlock(settings.copy(Blocks.IRON_BLOCK).nonOpaque()),
            new BlockItem.Settings().maxCount(1).food(FoodComponents.SWEET_BERRIES), // TODO: FIXME: JOKE
            ShowSelectorBlockEntity::new,
            new ShowSelectorModel<>()
    );

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        BlockEntityRendererFactories.register(REEL_HOLDER.entity(), ReelHolderRenderer::new);
        BlockEntityRendererFactories.register(SHOW_SELECTOR.entity(), ShowSelectorRenderer::new);
    }

    /** Keeps Java from ignoring the class */
    public static void registerBlocks() {}
}
