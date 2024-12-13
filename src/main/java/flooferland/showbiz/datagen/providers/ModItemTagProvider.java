package flooferland.showbiz.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @SuppressWarnings("CommentedOutCode")
    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
        //         .add(ModItems.SOME_EXAMPLE);
    }
}
