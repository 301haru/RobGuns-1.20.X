package net.robus.robguns.item.client.Flintlock;

import net.robus.robguns.item.mod_items.custom_items.FlintlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FlintlockRenderer extends GeoItemRenderer<FlintlockItem> {
    public FlintlockRenderer() {
        super(new FlintlockModel());
    }
}
