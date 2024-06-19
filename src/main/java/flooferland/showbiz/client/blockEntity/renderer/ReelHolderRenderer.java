package flooferland.showbiz.client.blockEntity.renderer;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.block.custom.ReelHolder;
import flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import flooferland.showbiz.client.blockEntity.model.ReelHolderModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.List;
import java.util.Optional;

public class ReelHolderRenderer extends GeoBlockRenderer<ReelHolderBlockEntity> {
    public ReelHolderRenderer(BlockEntityRendererFactory.Context context) {
        super(new ReelHolderModel<>());
    }

    @Override
    public void preRender(MatrixStack poseStack, ReelHolderBlockEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        Optional<GeoBone> reelHolder = model.getBone("reelHolder");
        GeoBone reels = reelHolder.map(geoBone -> model.searchForChildBone(geoBone, "reels")).orElse(null);

        // Changing the model
        for (int i = 1; i < animatable.getTargetInvSize()-1; i++) {
            boolean hasReel = !animatable.inventory.getStack(i).isEmpty();
            if (reels == null) {
                ShowbizMod.LOGGER.warn("the base path 'reelHolder/reels' does not exist in the reel holder model");
                continue;
            }

            GeoBone bone = model.searchForChildBone(reels, "reel" + i);
            if (bone != null) {
                bone.setHidden(!hasReel);
            } else {
                ShowbizMod.LOGGER.warn("could not find reel bone at 'reelHolder/reels/reel{}'", i);
            }
        }
    }

    @Override
    public void defaultRender(MatrixStack poseStack, ReelHolderBlockEntity animatable, VertexConsumerProvider bufferSource, @Nullable RenderLayer renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }
}
