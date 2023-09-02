package net.robus.robguns.item.client.Musket;

import net.robus.robguns.item.GeoGunItems.MusketItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MusketRenderer extends GeoItemRenderer<MusketItem> {

    public MusketRenderer() { super(new MusketModel()); }
}
