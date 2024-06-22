package flooferland.showbiz.backend.item.base;

import flooferland.showbiz.client.item.base.BlockEntityItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
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
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.model.GeoModel;
import java.util.function.Consumer;

public class BlockEntityItem <T extends BlockEntity & GeoBlockEntity> extends BlockItem implements GeoItem, GeoAnimatable {
    protected AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    protected AnimationController.AnimationStateHandler<BlockEntityItem<T>> animationHandler = null;
    protected GeoModel<GeoAnimatable> blockModel = null;

    public BlockEntityItem(Settings settings, Block block, GeoModel<GeoAnimatable> blockModel) {
        super(block, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.blockModel = blockModel;
    }
    
    public BlockEntityItem(
            Block block,
            Settings settings,
            AnimationController.AnimationStateHandler<BlockEntityItem<T>> animationHandler,
            GeoModel<GeoAnimatable> blockModel) {
        this(settings, block, blockModel);
        this.animationHandler = animationHandler;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            @Override
            public @NotNull BuiltinModelItemRenderer getGeoItemRenderer() {
                return new BlockEntityItemRenderer<>(
                        new GeoModel<BlockEntityItem<T>>() {
                            @Override
                            public Identifier getModelResource(BlockEntityItem animatable) {
                                return blockModel.getModelResource(animatable);
                            }

                            @Override
                            public Identifier getTextureResource(BlockEntityItem animatable) {
                                return blockModel.getTextureResource(animatable);
                            }

                            @Override
                            public Identifier getAnimationResource(BlockEntityItem animatable) {
                                return blockModel.getAnimationResource(animatable);
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
