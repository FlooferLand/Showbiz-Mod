package flooferland.showbiz.backend.recipe;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.recipe.custom.ReelAddTapeRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final ModRecipe<ShapelessRecipe.Serializer, ReelAddTapeRecipe> REEL_ADD_TAPE_RECIPE = register(
            "reel_add_tape_recipe",
            new ReelAddTapeRecipe.Serializer()
    );
    
    // region | Registry
    public record ModRecipe<S extends RecipeSerializer<?>, R extends net.minecraft.recipe.Recipe<?>>(S serializer, RecipeType<R> type) {}
    protected static <S extends RecipeSerializer<?>, R extends net.minecraft.recipe.Recipe<?>> ModRecipe<S, R> register(String id, S serializer) {
        Identifier identifier = Identifier.of(ShowbizMod.MOD_ID, id);
        RecipeType<R> recipeType = RecipeType.register(id);
        
        Registry.register(Registries.RECIPE_SERIALIZER, identifier, serializer);
        Registry.register(Registries.RECIPE_TYPE,identifier, recipeType);

        return new ModRecipe<>(serializer, recipeType);
    }
    // endregion
    
    /** Keeps Java from ignoring the class */
    public static void registerRecipes() {}
}
