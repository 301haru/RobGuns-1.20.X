package net.robus.robguns.item.client.GubGun;

import net.robus.robguns.item.mod_items.custom_items.BlunderbussItem;
import net.robus.robguns.item.mod_items.custom_items.GubGunItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GubGunRenderer extends GeoItemRenderer<GubGunItem> {

    public GubGunRenderer() { super(new GubGunModel()); }
}
