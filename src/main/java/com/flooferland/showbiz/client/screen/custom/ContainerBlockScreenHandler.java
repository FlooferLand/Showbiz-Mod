package com.flooferland.showbiz.client.screen.custom;

import com.flooferland.showbiz.backend.blockEntity.base.ContainerBlockEntity;
import com.flooferland.showbiz.client.registry.ModScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

/**
 * Simplifies the creation of container screen handles, allowing a container of any size. <br/>
 * Some code stolen from <a href="https://fabricmc.net/wiki/tutorial:containers">tutorial:containers</a>
 */
public class ContainerBlockScreenHandler extends ScreenHandler {
    private final @NotNull ContainerBlockEntity blockEntity;
    
    // Fast constructor; gets called with a BlockEntity directly and doesn't have to fetch it from the world
    public ContainerBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity handlerBlockEntity) {
        super(ModScreenHandlers.CONTAINER_BLOCK_SCREEN_HANDLER, syncId);
        
        this.blockEntity = (ContainerBlockEntity) handlerBlockEntity;        
        initializeScreen(playerInventory, blockEntity.inventory);
    }

    // Normal constructor
    public ContainerBlockScreenHandler(int syncId, PlayerInventory playerInventory, ContainerBlockEntity.CodecData codecData) {
        super(ModScreenHandlers.CONTAINER_BLOCK_SCREEN_HANDLER, syncId);
        
        blockEntity = ((ContainerBlockEntity)playerInventory.player.getWorld().getBlockEntity(codecData.pos()));
        if (blockEntity != null) { 
            initializeScreen(playerInventory, blockEntity.inventory);
        }
    }
    
    public Inventory getInventory() {
        return blockEntity.inventory;
    }

    public void initializeScreen(@NotNull PlayerInventory playerInventory, @NotNull Inventory inventory) {
        checkSize(inventory, blockEntity.getTargetInvSize());
        inventory.onOpen(playerInventory.player);

        // Rendering the block inventory
        int column = 0;
        int row = 0;
        for (int i = 0; i < inventory.size(); i++) {
            if (i % 9 == 0) {
                column = 0;
                row += 1;
            }
            this.addSlot(new Slot(inventory, i, 8 + column * 18, row * 18));
            column += 1;
        }

        // Player Inventory (27 storage + 9 hotbar)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, y * 9 + x + 9, 8 + x * 18, 18 + y * 18 + 103 + 18));
            }
        }

        // Player hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 18 + 161 + 18));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return getInventory().canPlayerUse(player);
    }
    
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        
        /*if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            Inventory inventory = getInventory();
            if (invSlot < inventory.size()) {
                if (!this.insertItem(originalStack, inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }*/

        return newStack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {        
        if (slot.inventory.hashCode() == blockEntity.inventory.hashCode()) {
            return blockEntity.checkStackCompatible(stack);
        }
        return true;
    }
}
