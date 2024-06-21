package flooferland.showbiz.backend.blockEntity.base;

import flooferland.showbiz.client.screen.custom.ContainerBlockScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/** Simplifies the creation of containers, and lets them share functionality */
public abstract class ContainerBlockEntity extends LootableContainerBlockEntity {
    public SimpleInventory inventory;
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
    public record CodecData(BlockPos pos) {
        public static final PacketCodec<RegistryByteBuf, CodecData> PACKET_CODEC =
                PacketCodec.tuple(BlockPos.PACKET_CODEC, CodecData::pos, CodecData::new);
    }
    
    protected ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    
    // region | Custom utility stuff
    /** Gets the inventory size this container is targeting (ex: <c>return 6;</c>)*/
    protected abstract int getTargetInvSize();
    
    // TODO: Implement this
    /** Should only return true if the stack can be placed inside this container */
    public boolean checkStackCompatible(ItemStack stack) {
        return true;
    }
    // endregion
    
    protected void initialiseInventory() {
        inventory = new SimpleInventory(getTargetInvSize());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        initialiseInventory();
        Inventories.readNbt(nbt, this.inventory.heldStacks, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory.heldStacks, registryLookup);
    }
    
    // region Container stuff
    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ContainerBlockScreenHandler(syncId, playerInventory, new CodecData(getPos()));
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerBlockScreenHandler(syncId, playerInventory, new CodecData(getPos()));
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return inventory.heldStacks;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> list) {
        inventory = new SimpleInventory(list.toArray(new ItemStack[0]));
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
