package flooferland.showbiz.datagen.providers;

import flooferland.showbiz.backend.block.ModBlocks;
import flooferland.showbiz.backend.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // region | Pizza recipe
        // TODO: Maybe move to a custom crafting system; don't use a crafting table for food because that's weird.
        {
            // More advanced, but it takes less resources
            ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModBlocks.PIZZA, 1)
                    .input(ModItems.PIE_CRUST)
                    .input(ModItems.MILK_BOTTLE)
                    .input(ModItems.TOMATO)
                    .criterion(hasItem(ModItems.MILK_BOTTLE), conditionsFromItem(ModItems.MILK_BOTTLE))
                    .offerTo(exporter, Identifier.of(getRecipeName(ModBlocks.PIZZA)));
            
            // A more vanilla recipe
            ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModBlocks.PIZZA, 1)
                    .input(Items.BREAD)
                    .input(Items.MILK_BUCKET)
                    .input(Items.BEETROOT)
                    .criterion(hasItem(Items.MILK_BUCKET), conditionsFromItem(Items.MILK_BUCKET))
                    .offerTo(exporter, Identifier.of(getRecipeName(ModBlocks.PIZZA) + "_vanilla"));
        }
        // endregion
        
        // region | Ore smelting
        // offerSmelting(exporter, List.of(ModBlocks.BAUXITE_ORE), RecipeCategory.MISC, ModItems.ALUMINUM_ALLOY,
        //         2,
        //         400,
        //         "misc"
        // );
        // offerBlasting(exporter, List.of(ModBlocks.BAUXITE_ORE), RecipeCategory.MISC, ModItems.ALUMINUM_ALLOY,
        //         3,
        //         128,
        //         "misc"
        // );
        // endregion
    }

    private static TagKey<Item> itemTagOf(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(id));
    }
}
