package net.robus.robguns.entity.mod_entities.custom_entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.robus.robguns.entity.mod_entities.GunProjectile;
import net.robus.robguns.item.ModItems;

public class RoundBallProjectile extends GunProjectile implements ItemSupplier {

    public RoundBallProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getRemainingFireTicks();

        if (entity.hurt(damageSources().generic(), getDamage())) {
            if (flag) {
                return;
            }

            if (disabledIFrames()) {
                entity.invulnerableTime = 0;
            }

            this.discard();
        } else {
            entity.setRemainingFireTicks(k);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                this.discard();
            }
        }
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
    public void tick() {
        super.tick();
        if (this.tickCount >= 300) {
            this.remove(Entity.RemovalReason.DISCARDED);
        }

        this.makeParticle(1);

        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult);

        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        if (this.level().getBlockStates(this.getBoundingBox()).anyMatch(BlockBehaviour.BlockStateBase::isSolid)) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale(1));
            this.setPos(d0, d1, d2);
        }
    }

    public ItemStack getItem() {
        return ModItems.ROUND_BALL.get().getDefaultInstance();
    }
}
