package net.robus.robguns.item.client.GubGun;

import net.minecraft.resources.ResourceLocation;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.mod_items.custom_items.BlunderbussItem;
import net.robus.robguns.item.mod_items.custom_items.GubGunItem;
import software.bernie.geckolib.model.GeoModel;

public class GubGunModel extends GeoModel<GubGunItem> {

    @Override
    public ResourceLocation getModelResource(GubGunItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "geo/gub_gun.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GubGunItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "textures/item/gub_gun_textures.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GubGunItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "animations/gub_gun.animation.json");
    }
}
