package flooferland.showbiz.backend.blockEntity.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Simplifies the creation of containers, and lets them share functionality */
public abstract class ContainerBlockEntity extends LootableContainerBlockEntity {
    protected DefaultedList<ItemStack> inventory;
    private final ViewerCountManager stateManager = new ViewerCountManager(){
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {}

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {}

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {}

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                return inventory == ContainerBlockEntity.this;
            }
            return false;
        }
    };
    
    protected ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    
    /** Gets the inventory size this container is targeting (ex: <c>return 6;</c>)*/
    protected abstract int getTargetInvSize(); 

    protected void initialiseInventory(BlockState state) {
        inventory = DefaultedList.ofSize(getTargetInvSize(), ItemStack.EMPTY);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        World world = getWorld();
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            initialiseInventory(state);
        } else {
            this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        }

        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
    }
    
    // region Container stuff
    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {  // Should createMenu be used?
        World world = getWorld();
        if (world == null) return null;

        // TODO: Port to a custom screen handler IMMEDIATELY
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, playerInventory, this, 1);
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> list) {
        inventory = list;
    }

    @Override
    public int size() {
        return inventory.size();
    }
    // endregion

    // region Basic container stuff
    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!this.removed) {
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }
    // endregion
}
