package com.flooferland.showbiz.backend.util;

import com.flooferland.showbiz.ShowbizMod;
import com.flooferland.showbiz.client.entity.custom.InteractPartEntity;
import com.flooferland.showbiz.backend.resource.MultiPartResourceReloader;
import com.flooferland.showbiz.backend.type.IMultiPartInteractable;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// TODO: Figure out a way to get interactions from the InteractPartEntity into the block entity itself

/**
 * Handles multipart interaction in blocks.
 * Also known as "controls"
 */
public final class MultiPartManager {
    /** Holds model interaction data for every part of nearly every block model in the mod */
    public static MultiPartResourceReloader.ModelMap multiPartData = null;
    @NotNull public Map<String, MultiPartResourceReloader.PartData> interactableParts = new HashMap<>();
    @NotNull public Map<String, Object> interactionMapping = new HashMap<>();
    @NotNull private final IMultiPartInteractable parent;
    private List<Integer> interactEntities = new ArrayList<>();
    private Identifier modelResourcePath;
    
    /** NOTE: Data from resource loaders is not available here! */
    public MultiPartManager(@NotNull IMultiPartInteractable self) {
        parent = self;
    }
    
    // TODO: Should try using `getType().getRegistryEntry().registryKey().getRegistry()` on `parent` instead, since it also gets the registry key
    public void init(Identifier modelResourcePath, boolean geoModel) {
        interactionMapping = parent.getInteractionMapping();
        this.modelResourcePath = geoModel ? modelResourcePath.withPrefixedPath("geckolib/models/block/").withSuffixedPath(".geo.json") : modelResourcePath;
    }
    public void init(Identifier modelResourcePath) {
        interactionMapping = parent.getInteractionMapping();
        this.modelResourcePath = modelResourcePath;
    }

    // CHECKME: Might want to check if this could be server-only or not
    /** Spawns all the interaction entities. Usually called during onPlaced */
    public void spawn(ClientWorld world, BlockPos parentBlock) {
        // Setup
        if (multiPartData.isEmpty()) {
            ShowbizMod.LOGGER.error("Data returned from the interaction resource reloader is empty. Interaction with blocks won't be possible, consider removing any resource packs.");
        }
        var modelParts = multiPartData.get(modelResourcePath);
        if (modelParts != null) {
            // Only adding parts that are in the interaction mapping (saves on memory)
            for (var entry : modelParts.entrySet()) {
                var partName = entry.getKey();
                var partData = entry.getValue();
                if (interactionMapping.containsKey(partName)) {
                    interactableParts.put(partName, partData);
                }
            }
        } else {
	        ShowbizMod.LOGGER.error("No multipart data found for the interactable block with model ID {}", modelResourcePath.toString());
        }
        
        // Killing any previous entities
        kill(world, parentBlock);
        
        // Spawning the entities
        for (var entry : interactableParts.entrySet()) {
            var name = entry.getKey();
            var part = entry.getValue();
            var parent = part.parent;
            
            var entity = new InteractPartEntity(world, name, part.pos, part.size, parentBlock);
            world.addEntity(entity);
            interactEntities.add(entity.getId());
        }
    }

    // CHECKME: Might want to check if this could be server-only or not
    /** Kills all the interaction entities */
    public void kill(ClientWorld world, BlockPos pos) {
        // Removing entity controls
        for (var id : interactEntities) {
            var entity = world.getEntityById(id);
            if (entity != null) entity.remove(Entity.RemovalReason.DISCARDED);
        }
        interactEntities.clear();
        
        // Remove left-over controls that might not be tied to a block
        for (var entity : getControlsNear(world, pos)){
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    /// Must be called on the block entity tick in order to update multipart entities based on distance
    public void tick(World world, BlockPos pos, BlockState state) {
        if (!(world instanceof ClientWorld clientWorld)) return;

        // Throttle world checks
        if (world.getTime() % (/* Update every */ 5 /* ticks */) != 0) return;

        // Get the local player
        var client = MinecraftClient.getInstance();
        if (client.player == null) return;

        var playerPos = client.player.getPos();
        double distance = playerPos.squaredDistanceTo(Vec3d.ofCenter(pos));
        float proximityRadius = maxInteractionReach(client.player);
        if (distance <= (proximityRadius * proximityRadius)) {
            spawn(clientWorld, pos);
        } else {
            kill(clientWorld, pos);
        }
    }
    
    /** Gets the controls near a position */
    public static @NotNull List<InteractPartEntity> getControlsNear(World world, BlockPos pos) {
        return world.getEntitiesByClass(InteractPartEntity.class, new Box(pos).expand(1.0), p -> true);
    }

    /// How close the player can be and still interact with the interaction entities
    public float maxInteractionReach(@Nullable PlayerEntity player) {
        return (player != null && player.isCreative()) ? 4f : 2.7f;
    }
}
