package net.robus.robguns.item.client.Musket;

import net.minecraft.resources.ResourceLocation;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.mod_items.custom_items.MusketItem;
import software.bernie.geckolib.model.GeoModel;

public class MusketModel extends GeoModel<MusketItem> {

    @Override
    public ResourceLocation getModelResource(MusketItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "geo/musket.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MusketItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "textures/item/musket_textures.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MusketItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "animations/musket.animation.json");
    }
}
