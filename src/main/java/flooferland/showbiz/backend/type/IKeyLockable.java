package flooferland.showbiz.backend.type;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IKeyLockable {
    void Lock(World world, BlockPos pos, BlockEntity blockEntity, PlayerEntity player);
    void Unlock(World world, BlockPos pos, BlockEntity blockEntity, PlayerEntity player);
    boolean IsLocked();
}
