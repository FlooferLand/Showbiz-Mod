package flooferland.showbiz.backend.registry;

import flooferland.showbiz.backend.item.FoodComponents;
import flooferland.showbiz.backend.item.custom.ReelItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;

/** Registry class for items */
public class ModItems {
    // region | Misc
    public static final ReelItem REEL = ShowbizRegistry.registerItem(
            "reel",
            (settings) -> new ReelItem(settings.maxCount(1))
    );
    public static final Item KEY = ShowbizRegistry.registerItem(
            "key",
        (settings) -> new Item(settings.maxCount(1))
    );
    // endregion
    
    // region | For technical recipes
    public static final Item MAGNETIC_TAPE = ShowbizRegistry.registerItem(
            "magnetic_tape",
        (settings) -> new Item(settings.food(new FoodComponent.Builder().nutrition(0).alwaysEdible().build()))
    );
    // endregion
    
    // region | For food recipes
    public static final Item PIE_CRUST = ShowbizRegistry.registerItem(
            "pie_crust",
        (settings) -> new Item(settings.food(new FoodComponent.Builder().nutrition(1).alwaysEdible().build()))
    );
    public static final Item MILK_BOTTLE = ShowbizRegistry.registerItem(
            "milk_bottle",
        (settings) -> new Item(settings.food(new FoodComponent.Builder().nutrition(1).alwaysEdible().build()))
    );
    public static final Item TOMATO = ShowbizRegistry.registerItem(
            "tomato",
        (settings) -> new Item(settings.food(new FoodComponent.Builder().nutrition(1).build()))
    );
    // endregion
    
    // region | Foods & drinks
    public static final Item PIZZA_SLICE = ShowbizRegistry.registerItem(
            "pizza_slice",
        (settings) -> new Item(settings.food(FoodComponents.PIZZA_SLICE))
    );
    // endregion

    /** Keeps Java from ignoring the class */
    public static void registerItems() {}
}
