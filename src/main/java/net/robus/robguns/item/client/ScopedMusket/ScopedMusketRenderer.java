package net.robus.robguns.item.client.ScopedMusket;

import net.robus.robguns.item.GeoGunItems.MusketItem;
import net.robus.robguns.item.client.Musket.MusketModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ScopedMusketRenderer extends GeoItemRenderer<MusketItem> {

    public ScopedMusketRenderer() { super(new MusketModel()); }
}
