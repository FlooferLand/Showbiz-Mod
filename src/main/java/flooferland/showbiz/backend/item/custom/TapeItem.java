package flooferland.showbiz.backend.item.custom;

import flooferland.showbiz.backend.item.base.ItemWithTape;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class TapeItem extends ItemWithTape {    
    public TapeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(getTapeLengthText(stack, "tape"));
    }
}
