package net.robus.robguns.item.client.Blunderbuss;

import net.minecraft.resources.ResourceLocation;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.GeoGunItems.BlunderbussItem;
import net.robus.robguns.item.GeoGunItems.MusketItem;
import software.bernie.geckolib.model.GeoModel;

public class BlunderbussModel extends GeoModel<BlunderbussItem> {

    @Override
    public ResourceLocation getModelResource(BlunderbussItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "geo/blunderbuss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlunderbussItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "textures/item/blunderbuss_textures.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlunderbussItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "animations/blunderbuss.animation.json");
    }
}
