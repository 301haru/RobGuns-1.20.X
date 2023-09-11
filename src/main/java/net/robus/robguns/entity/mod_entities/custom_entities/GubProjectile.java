package net.robus.robguns.entity.mod_entities.custom_entities;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.robus.robguns.entity.ModEntities;
import net.robus.robguns.entity.mod_entities.GunProjectile;

public class GubProjectile extends GunProjectile implements ItemSupplier {
    private int bounceAmount = 0;

    public GubProjectile(EntityType<? extends GunProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public GubProjectile(EntityType<? extends GunProjectile> pEntityType, Level pLevel, int bounceAmount) {
        super(pEntityType, pLevel);
        this.bounceAmount = bounceAmount;
    }


    public ItemStack getItem() {
        return Items.SLIME_BLOCK.getDefaultInstance();
    }

    @Override
    public void makeParticle(int pParticleAmount) {
        if (pParticleAmount > 0) {
            for(int j = 0; j < pParticleAmount; ++j) {
                this.level().addParticle(ParticleTypes.SNEEZE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D),
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

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult);

        Vec3 vec3 = this.getDeltaMovement();

        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        this.setDeltaMovement(vec3.scale(0.99f));

        Vec3 vec31 = this.getDeltaMovement();
        this.setDeltaMovement(vec31.x, vec31.y - 0.1f, vec31.z);
        this.setPos(d0, d1, d2);
        this.checkInsideBlocks();
    }

    @Override
    public void onHitBlock(BlockHitResult pResult) {
        if (bounceAmount > 0) {
            if (this.level() instanceof ServerLevel) {
                for (int i = 0; i < 3; i++) {
                    Vec3 dir = this.getDeltaMovement();

                    Vec3 dirNormal = dir.scale(0.6f);

                    if (pResult.getDirection() == Direction.UP || pResult.getDirection() == Direction.DOWN) {
                        dirNormal = (new Vec3(dirNormal.x, -dirNormal.y, dirNormal.z));
                    } else if (pResult.getDirection() == Direction.NORTH || pResult.getDirection() == Direction.SOUTH) {
                        dirNormal = (new Vec3(dirNormal.x, dirNormal.y, -dirNormal.z));
                    } else if (pResult.getDirection() == Direction.EAST || pResult.getDirection() == Direction.WEST) {
                        dirNormal = (new Vec3(-dirNormal.x, dirNormal.y, dirNormal.z));
                    }

                    double angleDegrees = 45 - (45 * i);
                    double angleRadians = Math.toRadians(angleDegrees);

                    double cosTheta = Math.cos(angleRadians);
                    double sinTheta = Math.sin(angleDegrees);

                    double rotatedX = cosTheta * dirNormal.x + sinTheta * dirNormal.z;
                    double rotatedY = dirNormal.y;
                    double rotatedZ = -sinTheta * dirNormal.x + cosTheta * dirNormal.z;

                    Vec3 rotatedDir = new Vec3(rotatedX, rotatedY, rotatedZ);

                    GubProjectile projectile = new GubProjectile(ModEntities.GUB_PROJECTILE.get(), this.level(), bounceAmount - 1);
                    projectile.setDeltaMovement(rotatedDir);
                    projectile.setPos(this.position());
                    projectile.setDamage(getDamage() / 1.5f);

                    double d0 = rotatedDir.horizontalDistance();
                    this.setYRot((float)(Mth.atan2(rotatedDir.x, rotatedDir.z) * (double)(180F / (float)Math.PI)));
                    this.setXRot((float)(Mth.atan2(rotatedDir.y, d0) * (double)(180F / (float)Math.PI)));
                    this.yRotO = this.getYRot();
                    this.xRotO = this.getXRot();

                    this.level().addFreshEntity(projectile);
                }
            }
        }
        this.level().playSound(this, this.getOnPos(), SoundEvents.SLIME_SQUISH, SoundSource.NEUTRAL, 1.0f, 1.0f);
        this.discard();
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
        }
    }

    @Override
    public void setNoGravity(boolean pNoGravity) {
        super.setNoGravity(false);
    }
}
