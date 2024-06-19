package flooferland.showbiz.client.blockEntity;

import flooferland.showbiz.client.blockEntity.renderer.ReelHolderRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import static flooferland.showbiz.backend.blockEntity.ModBlocksWithEntities.*;

public class ModClientBlocksComplex {
    public static void registerClientBlockEntities() {
        BlockEntityRendererFactories.register(REEL_HOLDER.entity, ReelHolderRenderer::new);
    }
}
