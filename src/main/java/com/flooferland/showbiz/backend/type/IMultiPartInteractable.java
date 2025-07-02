package com.flooferland.showbiz.backend.type;

import com.flooferland.showbiz.backend.util.MultiPartManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IMultiPartInteractable {
    public @NotNull Map<String, Object> getInteractionMapping();
    public @NotNull MultiPartManager getInteractionManager();
    public void onInteract(Object key, World world, PlayerEntity player);
}
