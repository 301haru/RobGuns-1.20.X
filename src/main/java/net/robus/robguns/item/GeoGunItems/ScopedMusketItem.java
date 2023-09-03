package net.robus.robguns.item.GeoGunItems;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.item.GeoGunItem;
import net.robus.robguns.item.client.Musket.MusketRenderer;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.Random;
import java.util.function.Consumer;

public class ScopedMusketItem extends GeoGunItem {
    private static final Random random = new Random();

    public ScopedMusketItem(Properties pProperties, int chargeTime, float damage) {
        super(pProperties);
        setChargeTime(chargeTime);
        setAttackDamage(damage);
        setTwoHanded(true);
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

    @Override
    public void shootParticle(LivingEntity entity, Level level) {
        double playerX = entity.getEyePosition().x;
        double playerY = entity.getEyePosition().y;
        double playerZ = entity.getEyePosition().z;

        double lookX = entity.getLookAngle().x;
        double lookY = entity.getLookAngle().y;
        double lookZ = entity.getLookAngle().z;

        double spawnX = playerX + lookX * 2;
        double spawnY = playerY + lookY * 2;
        double spawnZ = playerZ + lookZ * 2;

        for (int i = 0; i < 30; i++) {
            double offsetX = random.nextDouble() * 0.8 - 0.3;
            double offsetY = random.nextDouble() * 0.8 - 0.3;
            double offsetZ = random.nextDouble() * 0.8 - 0.3;

            level.addParticle(ParticleTypes.SMOKE, spawnX + offsetX, spawnY + offsetY, spawnZ + offsetZ, 0.0, 0.0, 0.0);
        }
    }
}
