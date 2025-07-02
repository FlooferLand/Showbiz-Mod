package com.flooferland.showbiz.client.blockEntity.renderer;

import com.flooferland.showbiz.ShowbizMod;
import com.flooferland.showbiz.backend.blockEntity.custom.ReelHolderBlockEntity;
import com.flooferland.showbiz.client.blockEntity.model.ReelHolderModel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class ReelHolderRenderer extends GeoBlockRenderer<ReelHolderBlockEntity> {
    public ReelHolderRenderer(BlockEntityRendererFactory.Context context) {
        super(new ReelHolderModel<>());
    }

    // @Override
    // public void renderChildBones(MatrixStack poseStack, ReelHolderBlockEntity animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
    //     super.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    // 
    //     poseStack.push();
    //     int i = 0;
    //     if (bone.getName().startsWith("reel")) {
    //         try {
    //             i = Integer.parseInt(bone.getName().replace("reel", ""));
    //         } catch (NumberFormatException err) {
    //             return;
    //         }
    //     }
    //         
    //     poseStack.push();
    //     boolean hasReel = !animatable.inventory.getStack(i).isEmpty();
    //     bone.setHidden(!hasReel);
    //     ShowbizMod.LOGGER.info("set reel {} to hidden={}", i, !hasReel);
    //     // ShowbizMod.LOGGER.info("reel at inventory slot {} is {}", i, animatable.inventory.getStack(i).toString());
    // }


    @Override
    public void preRender(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, int packedLight, int packedOverlay, int renderColor) {
        super.preRender(renderState, poseStack, model, bufferSource, buffer, isReRender, packedLight, packedOverlay, renderColor);

        // TODO: Add this back in / port it over
        // Reels only get set if reelHolder exists
        /*Optional<GeoBone> reelHolder = model.getBone("reelHolder");
        GeoBone reels = reelHolder.map(geoBone -> model.searchForChildBone(geoBone, "reels")).orElse(null);
    
        // Changing the model
        for (int i = 0; i < animatable.getTargetInvSize(); i++) {
            boolean hasReel = !animatable.inventory.getStack(i).isEmpty();
            if (reels == null) {
                ShowbizMod.LOGGER.warn("the base path 'reelHolder/reels' does not exist in the reel holder model");
                continue;
            }
    
            GeoBone bone = model.searchForChildBone(reels, "reel" + (i+1));
            if (bone != null) {
                bone.setHidden(!hasReel);
                //ShowbizMod.LOGGER.info("set reel {} to hidden={}", i, !hasReel);
                // ShowbizMod.LOGGER.info("reel at inventory slot {} is {}", i, animatable.inventory.getStack(i).toString());
            } else {
                String array = reels.getChildBones().stream().map(GeoBone::getName).collect(Collectors.joining(", "));;
                ShowbizMod.LOGGER.warn("could not find reel bone at 'reelHolder/reels/reel{}'\n\tFound the following instead of 'reel{}': [{}]", i+1, i+1, array);
            }
        }*/
    }
}
