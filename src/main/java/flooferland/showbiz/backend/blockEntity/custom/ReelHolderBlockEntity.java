package flooferland.showbiz.backend.blockEntity.custom;

import flooferland.showbiz.backend.block.custom.ReelHolderBlock;
import flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import flooferland.showbiz.backend.blockEntity.base.ContainerBlockEntity;
import flooferland.showbiz.backend.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class ReelHolderBlockEntity extends ContainerBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ReelHolderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocksWithEntities.REEL_HOLDER.entity(), pos, state);
        initialiseInventory();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.showbiz.reel_holder");
    }

    @Override
    public int getTargetInvSize() {
        return ReelHolderBlock.maxReelCount;
    }

    @Override
    public boolean checkStackCompatible(ItemStack stack) {
        return stack.isOf(ModItems.REEL);
    }
}
