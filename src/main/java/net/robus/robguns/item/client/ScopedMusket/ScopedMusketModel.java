package net.robus.robguns.item.client.ScopedMusket;

import net.minecraft.resources.ResourceLocation;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.GeoGunItems.ScopedMusketItem;
import software.bernie.geckolib.model.GeoModel;

public class ScopedMusketModel extends GeoModel<ScopedMusketItem> {

    @Override
    public ResourceLocation getModelResource(ScopedMusketItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "geo/scoped_musket.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ScopedMusketItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "textures/item/scoped_musket_textures.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ScopedMusketItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "animations/scoped_musket.animation.json");
    }
}
