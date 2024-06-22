package flooferland.showbiz.backend.item;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.item.base.ItemWithTape;
import flooferland.showbiz.backend.item.custom.KeyItem;
import flooferland.showbiz.backend.item.custom.ReelItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/** Registry class for items */
public class ModItems {
    // region | Misc
    public static final ReelItem REEL = registerItem(
            "reel",
            new ReelItem(new Item.Settings().maxCount(1))
    );
    public static final Item KEY = registerItem(
            "key",
            new KeyItem(new Item.Settings().maxCount(1))
    );
    // endregion
    
    // region | For technical recipes
    public static final Item MAGNETIC_TAPE = registerItem(
            "magnetic_tape",
            new ItemWithTape(new Item.Settings().food(new FoodComponent.Builder().nutrition(0).snack().build()))
    );
    // endregion
    
    // region | For food recipes
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
            new Item(new Item.Settings().food(FoodComponents.PIZZA_SLICE))
    );
    // endregion

    // region | Utility
    private static <T extends Item> T registerItem(String name) {
        //noinspection unchecked
        return registerItem(name, (T) new Item(new Item.Settings()));
    }
    private static <T extends Item> T registerItem(String name, T item) {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(ShowbizMod.MOD_ID, name),
                item
        );
    }
    
    /** Keeps Java from ignoring the class */
    public static void registerItems() {}
    // endregion
}
