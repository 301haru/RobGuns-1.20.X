package net.robus.robguns.item.client.Flintlock;

import net.minecraft.resources.ResourceLocation;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.GeoGunItems.FlintlockItem;
import software.bernie.geckolib.model.GeoModel;

public class FlintlockModel extends GeoModel<FlintlockItem> {

    @Override
    public ResourceLocation getModelResource(FlintlockItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "geo/flintlock.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FlintlockItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "textures/item/flintlock_textures.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FlintlockItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "animations/flintlock.animation.json");
    }
}
