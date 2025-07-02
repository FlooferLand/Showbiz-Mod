package com.flooferland.showbiz.backend.blockEntity.custom;

import com.flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import com.flooferland.showbiz.backend.type.IMultiPartInteractable;
import com.flooferland.showbiz.backend.util.MultiPartManager;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;

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
        for (int i = 0; i < buttonCount; i++) {
            map.put("buttonOn/"+i, i);
        }
        return map;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        multiPart.tick(world, pos, state);
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (!(world instanceof ClientWorld clientWorld)) return;
        multiPart.kill(clientWorld, pos);
    }

    @Override
    public void onInteract(Object k, World world, PlayerEntity player) {
        if (!(k instanceof Integer i)) return;
        
        // TODO: Once a show has been selected via a button, switch all other buttons to their `buttonOff` variants
        player.sendMessage(Text.of("Pressed button " + i), false);
    }
    // endregion
}
