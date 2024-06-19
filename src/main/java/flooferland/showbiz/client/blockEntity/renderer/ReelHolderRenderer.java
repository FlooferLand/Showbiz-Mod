package flooferland.showbiz.client.blockEntity.renderer;

import flooferland.showbiz.backend.block.custom.ReelHolder;
import flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import flooferland.showbiz.client.blockEntity.model.ReelHolderModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ReelHolderRenderer extends GeoBlockRenderer<ReelHolderBlockEntity> {
    public ReelHolderRenderer(BlockEntityRendererFactory.Context context) {
        super(new ReelHolderModel<>());
    }
}
