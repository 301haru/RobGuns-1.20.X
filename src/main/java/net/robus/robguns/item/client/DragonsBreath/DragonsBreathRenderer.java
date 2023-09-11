package net.robus.robguns.item.client.DragonsBreath;

import net.robus.robguns.item.client.Blunderbuss.BlunderbussModel;
import net.robus.robguns.item.mod_items.custom_items.BlunderbussItem;
import net.robus.robguns.item.mod_items.custom_items.DragonsBreathItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DragonsBreathRenderer extends GeoItemRenderer<DragonsBreathItem> {

    public DragonsBreathRenderer() { super(new DragonsBreathModel()); }
}
