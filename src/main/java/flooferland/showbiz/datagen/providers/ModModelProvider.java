package flooferland.showbiz.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        // generator.registerSimpleState(ModBlocks.EXAMPLE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        // generator.register(ModBlocksWithEntities.REEL_HOLDER.item, Models.GENERATED);
    }
}
