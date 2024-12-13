package flooferland.showbiz.client.blockEntity.renderer;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import flooferland.showbiz.backend.blockEntity.custom.ShowSelectorBlockEntity;
import flooferland.showbiz.client.blockEntity.model.ReelHolderModel;
import flooferland.showbiz.client.blockEntity.model.ShowSelectorModel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class ShowSelectorRenderer extends GeoBlockRenderer<ShowSelectorBlockEntity> {
    public ShowSelectorRenderer(BlockEntityRendererFactory.Context context) {
        super(new ShowSelectorModel<>());
    }
}
