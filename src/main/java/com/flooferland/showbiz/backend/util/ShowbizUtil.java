package com.flooferland.showbiz.backend.util;

import com.flooferland.showbiz.ShowbizMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Optional;

@SuppressWarnings("unused")
public final class ShowbizUtil {
    public static Random random = Random.create();
    
    /**
     * Switches the state of the held item.
     * Keeps the NBT data, and handles cases where multiple items are in the player's hand
    */
    public static void changeHandItemType(Hand hand, PlayerEntity player, Item newItemType) {
        ItemStack oldItem = player.getStackInHand(hand);
        ItemStack newItem = newItemType.getDefaultStack();
        var oldNbt = oldItem.getComponents().get(DataComponentTypes.CUSTOM_DATA);
        if (oldNbt != null) {
            newItem.applyComponentsFrom(
                    ComponentMap.builder()
                            .add(DataComponentTypes.CUSTOM_DATA, oldNbt)
                            .build()
            );
        }
        
        // Changing the item
        if (oldItem.getCount() > 1) {
            player.getInventory().insertStack(newItem);
            oldItem.decrement(1);
        } else {
            player.setStackInHand(hand, newItem);
        }
    }
    
    /** Returns coloured/formatted text formatted in a tooltippy way.
     * Uses <code>tooltip.showbiz.{id}</code> to look up an ID in your lang files
     * @param id A translatable ID that will be looked up
     * */
    public static Text tooltipText(String id) {
        return Text.translatable(String.format("tooltip.%s.%s", ShowbizMod.MOD_ID, id))
                .styled(style -> style.withColor(Formatting.DARK_AQUA));
    }

    /** Converts seconds into ticks */
    public static int secondsToTicks(double seconds) {
        return (int) (seconds * 20.0d);
    }
    
    /** Converts minutes into ticks */
    public static int minutesToTicks(float minutes) {
        return (int) ((minutes * 60.0f) * 20.0f);
    }

    /** Stolen from ChiseledBookshelfBlock. Mojang pls no sue */
    public static Optional<Vec2f> getHitPos(BlockHitResult hit, Direction facing) {
        Direction direction = hit.getSide();
        if (facing != direction)
            return Optional.empty();
        BlockPos blockPos = hit.getBlockPos().offset(direction);
        Vec3d vec3d = hit.getPos().subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        double x = vec3d.getX();
        double y = vec3d.getY();
        double z = vec3d.getZ();
        return switch (direction) {
            case NORTH -> Optional.of(new Vec2f((float)(1.0 - x), (float)y));
            case SOUTH -> Optional.of(new Vec2f((float)x, (float)y));
            case WEST -> Optional.of(new Vec2f((float)z, (float)y));
            case EAST -> Optional.of(new Vec2f((float)(1.0 - z), (float)y));
            case UP -> Optional.of(new Vec2f((float)z, (float)z)); // EXPERIMENTAL
            case DOWN -> Optional.empty();
        };
    }

    /** Scatters items when the block is destroyed */
    public static void scatterInventoryOnDestroy(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() == newState.getBlock()) return;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Inventory inventory) {
            ItemScatterer.spawn(world, pos, inventory);

            // FIXME?: This might not work, `state.getBlock()` should be `this` (method might need an argument passing in the block)
            world.updateComparators(pos, state.getBlock());
        }
    }

    /** Destroys the other top/bottom half of a DoubleBlockHalf block */
    public static void breakDoubleHalf(EnumProperty<DoubleBlockHalf> halfProperty, World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos otherHalfPos = state.get(halfProperty) == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState otherHalf = world.getBlockState(otherHalfPos);
        if (otherHalf.contains(halfProperty)) {
            world.setBlockState(otherHalfPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, otherHalfPos, Block.getRawIdFromState(otherHalf));
        }
    }
}
