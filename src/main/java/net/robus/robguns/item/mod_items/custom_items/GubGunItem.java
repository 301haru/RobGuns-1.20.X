package net.robus.robguns.item.mod_items.custom_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.entity.ModEntities;
import net.robus.robguns.entity.mod_entities.custom_entities.GubProjectile;
import net.robus.robguns.item.client.GubGun.GubGunRenderer;
import net.robus.robguns.item.mod_items.GunItem;
import net.robus.robguns.item.mod_items.SingleFireGunItem;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.Random;
import java.util.function.Consumer;

public class GubGunItem extends SingleFireGunItem {
    private static final Random random = new Random();

    public GubGunItem(Properties pProperties) {
        super(pProperties, 20, 20, 1f, true, false, 1f, 1);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GubGunRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GubGunRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void shoot(LivingEntity entity, Level level) {
        GubProjectile projectile = new GubProjectile(ModEntities.GUB_PROJECTILE.get(), level, 2);

        ItemStack itemStack = null;

        if (entity.getMainHandItem().getItem() instanceof GunItem) {
            itemStack = entity.getMainHandItem();
        } else {
            itemStack = entity.getOffhandItem();
        }

        projectile.setDamage(getAttackDamage(itemStack));

        Vec3 vec31 = entity.getUpVector(1.0F);
        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0 * ((float) Math.PI / 180F), vec31.x, vec31.y, vec31.z);
        Vec3 vec3 = entity.getViewVector(1.0F);
        Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);

        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), getProjectileVelocity(), getInaccuracy());
        projectile.setPos(entity.getEyePosition());

        level.addFreshEntity(projectile);

        shootParticle(entity, level);

        level.playSound(entity, entity.blockPosition(), SoundEvents.LLAMA_SPIT, SoundSource.PLAYERS, 0.8f, 1.2f);
        level.playSound(entity, entity.blockPosition(), SoundEvents.BEEHIVE_EXIT, SoundSource.PLAYERS, 1, 1);
    }

    @Override
    public void shootParticle(LivingEntity entity, Level level) {
        double playerX = entity.getEyePosition().x;
        double playerY = entity.getEyePosition().y;
        double playerZ = entity.getEyePosition().z;

        double lookX = entity.getLookAngle().x;
        double lookY = entity.getLookAngle().y;
        double lookZ = entity.getLookAngle().z;

        double spawnX = playerX + lookX * 1.5;
        double spawnY = playerY + lookY * 1.5;
        double spawnZ = playerZ + lookZ * 1.5;

        for (int i = 0; i < 20; i++) {
            double offsetX = random.nextDouble() * 0.6 - 0.3;
            double offsetY = random.nextDouble() * 0.6 - 0.3;
            double offsetZ = random.nextDouble() * 0.6 - 0.3;

            level.addParticle(ParticleTypes.SNEEZE, spawnX + offsetX, spawnY + offsetY, spawnZ + offsetZ, 0.0, 0.0, 0.0);
        }
    }

}
