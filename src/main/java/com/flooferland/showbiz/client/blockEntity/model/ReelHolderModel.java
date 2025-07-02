package com.flooferland.showbiz.client.blockEntity.model;

import com.flooferland.showbiz.ShowbizMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class ReelHolderModel<T extends GeoAnimatable> extends DefaultedBlockGeoModel<T> {
    public ReelHolderModel() {
        super(Identifier.of(ShowbizMod.MOD_ID, "reel_holder"));
    }
}
