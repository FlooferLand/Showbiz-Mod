package com.flooferland.showbiz.backend.item.custom;

import com.flooferland.showbiz.backend.item.base.ItemWithTape;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ReelItem extends ItemWithTape {    
    public ReelItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> tooltip, TooltipType type) {
        tooltip.accept(getTapeLengthText(stack, "reel"));
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, PlayerEntity player) {
        super.onCraftByPlayer(stack, player);
    }
}
