package net.robus.robguns.item.mod_items.custom_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.item.mod_items.ShotgunGunItem;
import net.robus.robguns.item.client.Blunderbuss.BlunderbussRenderer;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.function.Consumer;

public class BlunderbussItem extends ShotgunGunItem {
    public BlunderbussItem(Properties pProperties) {
        super(pProperties, true, 2.5f, 40, 10, true, 2.5f, 4f, 1);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlunderbussRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new BlunderbussRenderer();

                return this.renderer;
            }
        });
    }
}
