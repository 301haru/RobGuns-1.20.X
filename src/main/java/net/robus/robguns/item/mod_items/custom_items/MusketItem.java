package net.robus.robguns.item.mod_items.custom_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.item.mod_items.SingleFireGunItem;
import net.robus.robguns.item.client.Musket.MusketRenderer;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.function.Consumer;

public class MusketItem extends SingleFireGunItem {
    public MusketItem(Properties pProperties) {
        super(pProperties, 9, 35, 3.5f, true, false, 1f, 1);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private MusketRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new MusketRenderer();

                return this.renderer;
            }
        });
    }
}
