package flooferland.showbiz.backend.blockEntity.custom;

import flooferland.showbiz.backend.block.custom.ReelHolder;
import flooferland.showbiz.backend.blockEntity.ModBlocksWithEntities;
import flooferland.showbiz.backend.blockEntity.base.ContainerBlockEntity;
import flooferland.showbiz.backend.item.ModItems;
import flooferland.showbiz.client.screen.custom.ContainerBlockScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

// TODO: Separate parts into an abstract class or smth, so I wouldn't have to set up CODEC and cache every time.
//       Move the tick function into that class. its called by the block entity's normal block (getTicker) to make the block entities capable of ticking

public class ReelHolderBlockEntity extends ContainerBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    
    public ReelHolderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocksWithEntities.REEL_HOLDER.entity, pos, state);
        initialiseInventory();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 8, this::animationHandler));
    }

    @SuppressWarnings("SameReturnValue")
    private PlayState animationHandler(AnimationState<ReelHolderBlockEntity> state) {
        World world = getWorld();
        if (world == null) return PlayState.CONTINUE;

        // Getting the block
        BlockState block = world.getBlockState(pos);
        if (!(block.getBlock() instanceof ReelHolder))
            return PlayState.CONTINUE;
        
        
        // Getting & setting the animation
        // String key = block.get(ReelHolder.CAP_OPEN) ? "open" : "closed";
        // String animation = "animation.generator.cap_"+key;
        // state.getController().setAnimation(RawAnimation.begin().thenPlayAndHold(animation));
        return PlayState.CONTINUE;
    }

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
        return 9;
    }

    @Override
    protected boolean checkStackCompatible(ItemStack stack) {
        return stack.isOf(ModItems.REEL);
    }
    
    // TODO: Remove this function if we're going with a UI system for this
    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {  // Should createMenu be used?
        return null;
    }
}
