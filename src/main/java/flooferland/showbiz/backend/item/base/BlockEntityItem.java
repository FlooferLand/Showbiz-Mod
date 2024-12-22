package flooferland.showbiz.backend.item.base;

import flooferland.showbiz.client.item.base.BlockEntityItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

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
                return new BlockEntityItemRenderer<>(
                    new GeoModel<BlockEntityItem<T>>() {
                        @Override
                        public Identifier getModelResource(BlockEntityItem<T> geoAnimatable, @Nullable GeoRenderer<BlockEntityItem<T>> geoRenderer) {
                            return blockModel.getModelResource(geoAnimatable, geoRenderer);
                        }

                        @Override
                        public Identifier getTextureResource(BlockEntityItem<T> geoAnimatable, @Nullable GeoRenderer<BlockEntityItem<T>> geoRenderer) {
                            return blockModel.getTextureResource(geoAnimatable, geoRenderer);
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
            controllers.add(new AnimationController<>(this, "controller", 8, animationHandler));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
