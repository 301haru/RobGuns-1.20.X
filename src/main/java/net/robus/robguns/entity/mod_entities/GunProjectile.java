package net.robus.robguns.entity.mod_entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class GunProjectile extends Projectile {
    private boolean disableIFrames = false;
    private float damage = 4;

    public GunProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void makeParticle(int pParticleAmount) {
        if (pParticleAmount > 0) {

            for(int j = 0; j < pParticleAmount; ++j) {
                this.level().addParticle(ParticleTypes.CRIT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D),
                        0, 0, 0);
            }

        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.discard();
    }

    public void setDamage(float damage) { this.damage = damage; }
    public float getDamage() { return damage; }

    public void setDisableIFrame(boolean disableIFrame) { this.disableIFrames = disableIFrame; }
    public boolean disabledIFrames() { return disableIFrames; }


    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = this.getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 *= 64.0D * getViewScale();
        return pDistance < d0 * d0;
    }
}
