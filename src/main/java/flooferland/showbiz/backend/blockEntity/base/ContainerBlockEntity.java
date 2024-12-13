package flooferland.showbiz.backend.blockEntity.base;

import flooferland.showbiz.client.screen.custom.ContainerBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
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
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

// TODO: Separate parts into an abstract class or smth, so I wouldn't have to set up CODEC and cache every time.
//       Move the tick function into that class. its called by the block entity's normal block (getTicker) to make the block entities capable of ticking

/** Simplifies the creation of containers, and lets them share functionality */
public abstract class ContainerBlockEntity extends LockableContainerBlockEntity implements ExtendedScreenHandlerFactory<ContainerBlockEntity.CodecData> {
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
    public abstract int getTargetInvSize();
    
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
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerBlockScreenHandler(syncId, playerInventory, this);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ContainerBlockScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public CodecData getScreenOpeningData(ServerPlayerEntity player) {
        return new CodecData(getPos());
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
