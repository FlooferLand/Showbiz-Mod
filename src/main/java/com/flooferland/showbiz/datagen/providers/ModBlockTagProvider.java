package com.flooferland.showbiz.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @SuppressWarnings("CommentedOutCode")
    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
        //         .add(ModBlocks.SOME_EXAMPLE);
    }
}
