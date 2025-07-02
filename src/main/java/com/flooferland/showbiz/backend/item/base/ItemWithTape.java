package com.flooferland.showbiz.backend.item.base;

import com.flooferland.showbiz.backend.registry.ModDataComponents;
import com.flooferland.showbiz.backend.util.ShowbizUtil;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/** An item that has a tape length component and displays and calculates the tape length */
public class ItemWithTape extends Item {
    public ItemWithTape(Settings settings) {
        super(settings);
    }

    // region | Stack creator thingies
    @Override
    public ItemStack getDefaultStack() {
        return getDefaultStack(0);
    }

    public ItemStack getDefaultStack(int tapeLength) {
        ItemStack stack = new ItemStack(this);
        stack.applyComponentsFrom(
                ComponentMap.builder()
                        .add(ModDataComponents.TAPE_LENGTH, tapeLength)
                        .build()
        );
        return stack;
    }
    // endregin
    
    public Text getTapeLengthText(ItemStack stack, String itemName) {
        int length = stack.getComponents().getOrDefault(ModDataComponents.TAPE_LENGTH, 0);
        return Text.of(ShowbizUtil.tooltipText(itemName + ".length").getString().formatted(length));
    }
}
