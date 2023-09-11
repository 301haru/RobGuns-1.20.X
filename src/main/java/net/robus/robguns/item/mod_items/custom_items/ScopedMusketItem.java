package net.robus.robguns.item.mod_items.custom_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.item.mod_items.SingleFireGunItem;
import net.robus.robguns.item.client.ScopedMusket.ScopedMusketRenderer;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.Random;
import java.util.function.Consumer;

public class ScopedMusketItem extends SingleFireGunItem {
    public ScopedMusketItem(Properties pProperties) {
        super(pProperties, 16, 40, 3.9f, true, true, 0.1f, 0.15f);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ScopedMusketRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new ScopedMusketRenderer();

                return this.renderer;
            }
        });
    }
}
