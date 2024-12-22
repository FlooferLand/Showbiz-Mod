package flooferland.showbiz.datagen.providers;

import flooferland.showbiz.backend.registry.ModBlocks;
import flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import flooferland.showbiz.backend.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

// TODO: Add survival recipes for all of the blocks

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {
                // region | Reels and tapes
                // Magnetic tape
                createShapeless(RecipeCategory.MISC, ModItems.MAGNETIC_TAPE, 1)
                    .input(Items.DRIED_KELP)
                    .input(Items.IRON_NUGGET)
                    .input(Items.COPPER_INGOT)
                    .criterion(hasItem(Items.KELP), conditionsFromItem(Items.KELP))
                    .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(getRecipeName(ModItems.MAGNETIC_TAPE))));

                // Reels
                createShapeless(RecipeCategory.MISC, ModItems.REEL, 1)
                    .input(ModItems.MAGNETIC_TAPE)
                    .input(ItemTags.WOODEN_SLABS)
                    .input(Items.IRON_NUGGET)
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(getRecipeName(ModItems.REEL))));
                // Reels
                createShaped(RecipeCategory.MISC, ModItems.REEL, 1)
                    .pattern("   ")
                    .pattern("_ _")
                    .pattern("___")
                    .input('_', ItemTags.WOODEN_SLABS)
                    .criterion(hasItem(Items.OAK_SLAB), conditionsFromItem(Items.OAK_SLAB))
                    .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(getRecipeName(ModBlocksWithEntities.REEL_HOLDER.item()))));
                // endregion

                // region | Pizza recipe
                // TODO: Maybe move to a custom crafting system; don't use a crafting table for food because that's weird.
                {
                    // More advanced, but it takes less resources
                    createShapeless(RecipeCategory.FOOD, ModBlocks.PIZZA, 1)
                        .input(ModItems.PIE_CRUST)
                        .input(ModItems.MILK_BOTTLE)
                        .input(ModItems.TOMATO)
                        .criterion(hasItem(ModItems.MILK_BOTTLE), conditionsFromItem(ModItems.MILK_BOTTLE))
                        .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(getRecipeName(ModBlocks.PIZZA))));

                    // A more vanilla recipe
                    createShapeless(RecipeCategory.FOOD, ModBlocks.PIZZA, 1)
                        .input(Items.BREAD)
                        .input(Items.MILK_BUCKET)
                        .input(Items.BEETROOT)
                        .criterion(hasItem(Items.MILK_BUCKET), conditionsFromItem(Items.MILK_BUCKET))
                        .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(getRecipeName(ModBlocks.PIZZA) + "_vanilla")));
                }
                // endregion

                // region | Ore smelting
                // offerSmelting(List.of(ModBlocks.BAUXITE_ORE), RecipeCategory.MISC, ModItems.ALUMINUM_ALLOY,
                //         2,
                //         400,
                //         "misc"
                // );
                // offerBlasting(List.of(ModBlocks.BAUXITE_ORE), RecipeCategory.MISC, ModItems.ALUMINUM_ALLOY,
                //         3,
                //         128,
                //         "misc"
                // );
                // endregion
            }
        };
    }

    private static TagKey<Item> itemTagOf(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(id));
    }

    @Override
    public String getName() {
        return "ShowbizMod Recipes";
    }
}
