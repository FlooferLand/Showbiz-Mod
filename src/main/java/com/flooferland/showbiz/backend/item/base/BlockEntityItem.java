package com.flooferland.showbiz.backend.item.base;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.function.Consumer;

public class BlockEntityItem <T extends BlockEntity & GeoBlockEntity> extends BlockItem implements GeoItem, GeoAnimatable {
    protected AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    protected AnimationController.AnimationStateHandler<BlockEntityItem<T>> animationHandler = null;
    protected GeoModel<BlockEntityItem<T>> blockModel = null;

    public BlockEntityItem(Settings settings, Block block, GeoModel<BlockEntityItem<T>> blockModel) {
        super(block, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.blockModel = blockModel;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            @Override
            public @NotNull GeoItemRenderer<?> getGeoItemRenderer() {
                return new GeoItemRenderer<>(
                    new GeoModel<BlockEntityItem<T>>() {
                        @Override
                        public Identifier getModelResource(GeoRenderState geoRenderState) {
                            return blockModel.getModelResource(geoRenderState);
                        }

                        @Override
                        public Identifier getTextureResource(GeoRenderState geoRenderState) {
                            return blockModel.getTextureResource(geoRenderState);
                        }

                        @Override
                        public Identifier getAnimationResource(BlockEntityItem<T> geoAnimatable) {
                            return blockModel.getAnimationResource(geoAnimatable);
                        }
                    }
                );
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        if (animationHandler != null) {
            controllers.add(new AnimationController<>("controller", 8, animationHandler));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
