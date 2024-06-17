package flooferland.showbiz.backend.item;

import net.minecraft.component.type.FoodComponent;

public class FoodComponents {
    public static final FoodComponent PIZZA_SLICE = new FoodComponent.Builder()
            .nutrition(5).saturationModifier(0f)
            .build();
}
