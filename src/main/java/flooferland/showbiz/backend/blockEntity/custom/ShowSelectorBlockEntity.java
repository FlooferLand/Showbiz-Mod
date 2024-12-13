package flooferland.showbiz.backend.blockEntity.custom;

import flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import flooferland.showbiz.backend.type.IMultiPartInteractable;
import flooferland.showbiz.backend.util.MultiPartManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.HashMap;
import java.util.Map;

public class ShowSelectorBlockEntity extends BlockEntity implements GeoBlockEntity, IMultiPartInteractable {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public MultiPartManager multiPart = new MultiPartManager(this);
    
    public ShowSelectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocksWithEntities.SHOW_SELECTOR.entity(), pos, state);
        multiPart.init(ModBlocksWithEntities.SHOW_SELECTOR.id(), true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // region | Multi-part interaction controls
    @Override
    public @NotNull MultiPartManager getInteractionManager() {
        return multiPart;
    }
    
    @Override
    public @NotNull Map<String, Object> getInteractionMapping() {
        final int buttonCount = 12;
        var map = new HashMap<String, Object>();
        for (int i = 0; i < buttonCount-1; i++) {
            map.put("buttonOn/"+i, i);
        }
        return map;
    }

    @Override
    public void onInteract(Object k, World world, PlayerEntity player) {
        if (!(k instanceof Integer i)) return;
        
        player.sendMessage(Text.of("Pressed button " + i));
    }
    // endregion
}
