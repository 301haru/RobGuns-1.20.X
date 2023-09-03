package net.robus.robguns.item.client.Blunderbuss;

import net.robus.robguns.item.GeoGunItems.BlunderbussItem;
import net.robus.robguns.item.GeoGunItems.MusketItem;
import net.robus.robguns.item.client.Musket.MusketModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BlunderbussRenderer extends GeoItemRenderer<BlunderbussItem> {

    public BlunderbussRenderer() { super(new BlunderbussModel()); }
}
