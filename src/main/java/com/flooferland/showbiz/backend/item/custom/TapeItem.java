package com.flooferland.showbiz.backend.item.custom;

import com.flooferland.showbiz.backend.item.base.ItemWithTape;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class TapeItem extends ItemWithTape {    
    public TapeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(getTapeLengthText(stack, "tape"));
    }
}
