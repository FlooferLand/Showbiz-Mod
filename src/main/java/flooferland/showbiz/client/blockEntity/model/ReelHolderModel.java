package flooferland.showbiz.client.blockEntity.model;

import flooferland.showbiz.ShowbizMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class ReelHolderModel<T extends GeoAnimatable> extends GeoModel<T> {
    @Override
    public Identifier getModelResource(T animatable) {
        return Identifier.of(ShowbizMod.MOD_ID, "geo/reel_holder.geo.json");
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        return Identifier.of(ShowbizMod.MOD_ID, "textures/block/reel_holder.png");
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return Identifier.of(ShowbizMod.MOD_ID, "");
    }
}
