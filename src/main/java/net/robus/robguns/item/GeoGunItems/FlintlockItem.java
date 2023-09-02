package net.robus.robguns.item.GeoGunItems;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.item.GeoGunItem;
import net.robus.robguns.item.client.Flintlock.FlintlockRenderer;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.function.Consumer;

public class FlintlockItem extends GeoGunItem {

    public FlintlockItem(Properties pProperties, int chargeTime, float damage) {
        super(pProperties);
        setChargeTime(chargeTime);
        setAttackDamage(damage);
        setTwoHanded(false);
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
