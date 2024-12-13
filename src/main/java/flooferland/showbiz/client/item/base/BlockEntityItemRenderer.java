package flooferland.showbiz.client.item.base;

import net.minecraft.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BlockEntityItemRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
    public BlockEntityItemRenderer(GeoModel<T> model) {
        super(model);
    }
}
