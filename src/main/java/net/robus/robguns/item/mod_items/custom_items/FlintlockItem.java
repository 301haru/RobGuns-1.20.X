package net.robus.robguns.item.mod_items.custom_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.item.mod_items.SingleFireGunItem;
import net.robus.robguns.item.client.Flintlock.FlintlockRenderer;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.function.Consumer;

public class FlintlockItem extends SingleFireGunItem {
    public FlintlockItem(Properties pProperties) {
        super(pProperties, 8, 30, 3f, false, false, 1f, 1);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private FlintlockRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new FlintlockRenderer();

                return this.renderer;
            }
        });
    }
}
