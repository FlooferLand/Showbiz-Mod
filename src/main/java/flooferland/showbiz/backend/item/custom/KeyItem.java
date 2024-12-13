package flooferland.showbiz.backend.item.custom;

import flooferland.showbiz.backend.type.IKeyLockable;
import flooferland.showbiz.backend.util.ShowbizUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class KeyItem extends Item {
    public KeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        PlayerEntity player = ctx.getPlayer();
        if (world.isClient || player == null) return ActionResult.FAIL;
        
        BlockEntity blockEntity = world.getBlockEntity(pos);
        IKeyLockable keyLockable = ((IKeyLockable) blockEntity);
        if (keyLockable != null) {
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS);
            if (keyLockable.IsLocked()) {
                keyLockable.Unlock(world, pos, blockEntity, player);
            } else {
                keyLockable.Lock(world, pos, blockEntity, player);
            }
            return ActionResult.SUCCESS;
        }
        
        return ActionResult.FAIL;
    }
}
