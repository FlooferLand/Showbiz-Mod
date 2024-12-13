package flooferland.showbiz.backend.recipe.custom;

import flooferland.showbiz.backend.registry.ModItems;
import flooferland.showbiz.backend.registry.ModRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class ReelAddTapeRecipe extends SpecialCraftingRecipe {

    public ReelAddTapeRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        System.out.println("Checking ReelAddTapeRecipe");
        boolean has_reel = false;
        boolean has_tape = false;
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack itemStack = input.getStackInSlot(i);
            if (itemStack.isEmpty()) continue;
            if (itemStack.isOf(ModItems.REEL)) has_reel = true;
            if (itemStack.isOf(ModItems.MAGNETIC_TAPE)) has_tape = true;
        }
        return has_reel && has_tape;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        int tape_length = 0;
        return ModItems.REEL.getDefaultStack(tape_length);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.REEL_ADD_TAPE_RECIPE;
    }
}