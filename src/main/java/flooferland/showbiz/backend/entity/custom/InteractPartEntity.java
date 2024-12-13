package flooferland.showbiz.backend.entity.custom;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.networking.InteractPartPayload;
import flooferland.showbiz.backend.registry.ModEntities;
import flooferland.showbiz.backend.type.IMultiPartInteractable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: Maybe use BlockAttachedEntity instead of Entity

/**
 * Similar to Minecraft's InteractionEntity / DisplayEntity.
 * Simple light-weight entity to register physical interactions.
 * Spawned and filled out by {@code MultiPartManager}
 */
@Environment(EnvType.CLIENT)
public class InteractPartEntity extends Entity {
    @Nullable public String name = null;
    @Nullable public Vec2f size = null;
    @Nullable public BlockPos parentBlock = null;
    
    public InteractPartEntity(World world) {
        super(ModEntities.INTERACT_PART, world);
    }

    public InteractPartEntity(World world, @NotNull String name, @NotNull Vec3d pos, @NotNull Vec2f size, @NotNull BlockPos parentBlock) {
        this(world);
        this.name = name;
        this.size = size;
        this.parentBlock = parentBlock;
        this.reinitDimensions();
        this.calculateDimensions();
        this.setPosition(parentBlock.toCenterPos().x + pos.x, parentBlock.toCenterPos().y + pos.y, parentBlock.toCenterPos().z + pos.z);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        if (size != null) {
            return EntityDimensions.fixed(size.x, size.y);
        }
        return super.getDimensions(pose);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        var world = player.getWorld();
        if (!world.isClient()) return ActionResult.FAIL;  // Client-side interaction
        
        // Triggering interaction
        if (world.getBlockEntity(parentBlock) instanceof IMultiPartInteractable interact) {
            var mapping = interact.getInteractionManager().interactionMapping;
            if (mapping.containsKey(name) && parentBlock != null) {
                var packet = new InteractPartPayload(name, parentBlock, player.getUuid());
                ClientPlayNetworking.send(packet);
                return ActionResult.SUCCESS;
            }
        } else {
            // TODO: Remove the entity here because its block can't be found
            return ActionResult.FAIL;
        }
        return ActionResult.FAIL;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true; // return super.shouldRender(distance);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
    }

    @Override
    public void onRemoved() {
        // TODO: Make the entity respawn if its killed while its block still exists (means like `/kill`, etc)
        super.onRemoved();
    }

    @Override
    public Text getName() {
        return (name != null) ? Text.of(name) : getDefaultName();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    // region | Lots of flags
    @Override
    public boolean canHit() {
        return true;
    }

    // TODO: Test if projectile collision affects performance at scale
    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    @Override
    public boolean canAvoidTraps() {
        return true;
    }

    @Override
    public boolean canBeSpectated(ServerPlayerEntity spectator) {
        return false;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
    // endregion
}
