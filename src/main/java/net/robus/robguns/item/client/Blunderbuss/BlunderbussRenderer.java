package net.robus.robguns.item.client.Blunderbuss;

import net.robus.robguns.item.mod_items.custom_items.BlunderbussItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BlunderbussRenderer extends GeoItemRenderer<BlunderbussItem> {

    public BlunderbussRenderer() { super(new BlunderbussModel()); }
}
