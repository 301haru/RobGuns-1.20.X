package net.robus.robguns.item.client.ScopedMusket;

import net.robus.robguns.item.mod_items.custom_items.ScopedMusketItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ScopedMusketRenderer extends GeoItemRenderer<ScopedMusketItem> {

    public ScopedMusketRenderer() { super(new ScopedMusketModel()); }
}
