package flooferland.showbiz.client.screen.custom;

import flooferland.showbiz.backend.blockEntity.base.ContainerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

/**
 * Simplifies the creation of container screen handles, allowing a container of any size. <br/>
 * Some code stolen from <a href="https://fabricmc.net/wiki/tutorial:containers">tutorial:containers</a>
 */
public class ContainerBlockScreenHandler extends ScreenHandler {
    private final ContainerBlockEntity blockEntity;
    
    public ContainerBlockScreenHandler(int syncId, PlayerInventory inventory, ContainerBlockEntity.CodecData data) {
        this(syncId, inventory, (ContainerBlockEntity) inventory.player.getWorld().getBlockEntity(data.pos()), new ArrayPropertyDelegate(2));
    }
    
    public ContainerBlockScreenHandler(int syncId, PlayerInventory playerInventory, ContainerBlockEntity blockEntity, PropertyDelegate delegate) {
        super(null, syncId);
        int x, y;
        
        checkSize(blockEntity.inventory, 2);
        this.blockEntity = blockEntity;
        this.blockEntity.inventory.onOpen(playerInventory.player);

        // Creating slots bellow --
        
        // Chest Inventory
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 9; x++) {
                this.addSlot(new Slot(blockEntity.inventory, y * 9 + x, 8 + x * 18, 18 + y * 18));
            }
        }

        // Player Inventory (27 storage + 9 hotbar)
        for (y = 0; y < 3; y++) {
            for (x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, y * 9 + x + 9, 8 + x * 18, 18 + y * 18 + 103 + 18));
            }
        }
        
        // Player hotbar
        for (x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 18 + 161 + 18));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return blockEntity.inventory.canPlayerUse(player);
    }
    
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < blockEntity.inventory.size()) {
                if (!this.insertItem(originalStack, blockEntity.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, blockEntity.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        if (slot.inventory == blockEntity.inventory) {
            return blockEntity.checkStackCompatible(stack);
        }
        return true;
    }
}
