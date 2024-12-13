package flooferland.showbiz.backend.block.base;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class EntityTiedBlock extends BlockWithEntity {
    public static MapCodec<ContainerBlock> CODEC = null;
    public ICreateBlockEntity BLOCK_ENTITY_MAKER = null;
    public interface ICreateBlockEntity {
        BlockEntity createBlockEntity(BlockPos pos, BlockState state);
    }

    public EntityTiedBlock(Settings settings, ICreateBlockEntity blockEntity) {
        super(settings);
        BLOCK_ENTITY_MAKER = blockEntity;
    }
    
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BLOCK_ENTITY_MAKER.createBlockEntity(pos, state);
    }
}
