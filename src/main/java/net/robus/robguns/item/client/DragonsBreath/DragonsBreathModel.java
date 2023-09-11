package net.robus.robguns.item.client.DragonsBreath;

import net.minecraft.resources.ResourceLocation;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.mod_items.custom_items.BlunderbussItem;
import net.robus.robguns.item.mod_items.custom_items.DragonsBreathItem;
import software.bernie.geckolib.model.GeoModel;

public class DragonsBreathModel extends GeoModel<DragonsBreathItem> {

    @Override
    public ResourceLocation getModelResource(DragonsBreathItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "geo/dragons_breath.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonsBreathItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "textures/item/dragons_breath_textures.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonsBreathItem animatable) {
        return new ResourceLocation(RobGuns.MODID, "animations/dragons_breath.animation.json");
    }
}
