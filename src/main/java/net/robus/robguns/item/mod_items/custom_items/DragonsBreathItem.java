package net.robus.robguns.item.mod_items.custom_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.entity.ModEntities;
import net.robus.robguns.entity.mod_entities.custom_entities.DragonsBreathProjectile;
import net.robus.robguns.item.client.Blunderbuss.BlunderbussRenderer;
import net.robus.robguns.item.client.DragonsBreath.DragonsBreathRenderer;
import net.robus.robguns.item.mod_items.GunItem;
import net.robus.robguns.item.mod_items.ShotgunGunItem;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.Random;
import java.util.function.Consumer;

public class DragonsBreathItem extends ShotgunGunItem {
    public DragonsBreathItem(Properties pProperties) {
        super(pProperties, false, 0.5f, 40, 20, true, 2f, 25f, 1);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DragonsBreathRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new DragonsBreathRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void shoot(LivingEntity entity, Level level) {
        ItemStack itemStack = null;

        if (entity.getMainHandItem().getItem() instanceof GunItem) {
            itemStack = entity.getMainHandItem();
        } else {
            itemStack = entity.getOffhandItem();
        }

        for (int i = 0; i < getProjectileAmount(); i++) {
            DragonsBreathProjectile projectile = new DragonsBreathProjectile(ModEntities.DRAGONS_BREATH_PROJECTILE.get(), level);

            projectile.setDamage(getAttackDamage(itemStack));
            projectile.setDisableIFrame(DisabledIFrames());

            Vec3 vec31 = entity.getUpVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0 * ((float) Math.PI / 180F), vec31.x, vec31.y, vec31.z);
            Vec3 vec3 = entity.getViewVector(1.0F);
            Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);

            projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 2.5f, getInaccuracy());
            projectile.setPos(entity.getEyePosition());

            level.addFreshEntity(projectile);
        }

        shootParticle(entity, level);

        level.playSound(entity, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.8f, 1.2f);
        level.playSound(entity, entity.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1, 1);
    }
}
