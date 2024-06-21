package flooferland.showbiz.backend.recipe.custom;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ReelAddTapeRecipe extends ShapelessRecipe {
    public ReelAddTapeRecipe(String group, CraftingRecipeCategory category, ItemStack result, DefaultedList<Ingredient> ingredients) {
        super(group, category, result, ingredients);
    }
    
    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ShowbizMod.LOGGER.info("NO BRO");
        if (!super.matches(input, world)) {
            return false;
        }
        
        boolean hasTape = false, hasReel = false;
        for (ItemStack stack : input.getStacks()) {
            if (stack.isOf(ModItems.MAGNETIC_TAPE)) hasTape = true;
            if (stack.isOf(ModItems.REEL)) hasReel = true;
        }
        
        return hasTape && hasReel;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup wrapperLookup) {
        int tapeLength = 0;
        
        return ModItems.REEL.getDefaultStack(tapeLength);
    }
    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

}