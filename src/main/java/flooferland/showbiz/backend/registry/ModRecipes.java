package flooferland.showbiz.backend.registry;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.recipe.custom.ReelAddTapeRecipe;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

    public static final RecipeSerializer<ReelAddTapeRecipe> REEL_ADD_TAPE_RECIPE = register("reel_add_tape_recipe", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(ReelAddTapeRecipe::new));

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        Identifier identifier = Identifier.of(ShowbizMod.MOD_ID, id);
        return Registry.register(Registries.RECIPE_SERIALIZER, identifier, serializer);
    }
    // endregion
    
    /** Keeps Java from ignoring the class */
    public static void registerRecipes() {}
}
