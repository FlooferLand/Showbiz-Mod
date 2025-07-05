package com.flooferland.showbiz.datagen.providers;

import com.flooferland.showbiz.backend.registry.ModItems;
import com.flooferland.showbiz.backend.registry.ModJukeboxSongs;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

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
        generator.register(ModItems.KEY, Models.GENERATED);
        generator.register(ModJukeboxSongs.Y2K.item, Models.TEMPLATE_MUSIC_DISC);
    }
}
