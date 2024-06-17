package flooferland.showbiz.backend.item;

import flooferland.showbiz.ShowbizMod;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // region | Misc
    // endregion
    
    // region | For recipes
    public static final Item PIE_CRUST = registerItem(
            "pie_crust",
            new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(1).snack().build()))
    );
    public static final Item MILK_BOTTLE = registerItem(
            "milk_bottle",
            new PotionItem(new Item.Settings().food(new FoodComponent.Builder().nutrition(1).snack().build()))
    );
    public static final Item TOMATO = registerItem(
            "tomato",
            new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(1).snack().build()))
    );
    // endregion
    
    // region | Foods & drinks
    public static final Item PIZZA_SLICE = registerItem(
            "pizza_slice",
            new Item(new Item.Settings().food(ModFoodComponents.PIZZA_SLICE))
    );
    // endregion

    // region | Utility
    private static Item registerItem(String name, Item item) {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(ShowbizMod.MOD_ID, name),
                item
        );
    }
    @SuppressWarnings("unused") public static void registerItems() {}
    // endregion
}
