package flooferland.showbiz.datagen.providers;

import flooferland.showbiz.ShowbizMod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        // generator.registerSimpleState(ModBlocks.EXAMPLE);
    }
    
    public final Model getBlockModelFromIdPath(String path, Optional<String> variant, TextureKey texKey) {
        return new Model(Optional.of(Identifier.of(ShowbizMod.MOD_ID, "block/" + path)), variant, texKey);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        // generator.register(ModBlocks.EXAMPLE.asItem(), Models.GENERATED);
    }
}
