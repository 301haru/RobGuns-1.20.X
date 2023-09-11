package net.robus.robguns.item.mod_items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.robus.robguns.entity.ModEntities;
import net.robus.robguns.entity.mod_entities.custom_entities.RoundBallProjectile;
import net.robus.robguns.item.ModItems;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.Random;

public class ShotgunGunItem extends GeoGunItem {
    private int projectileAmount = 5;
    private boolean disableIFrames = false;

    private static final Random random = new Random();

    public ShotgunGunItem(Properties pProperties, boolean twoHanded, float damage, int chargeTime, int projectileAmount,
                          boolean disableIFrames, float projectileVelocity, float inaccuracy, float fovModifier) {
        super(pProperties);
        setChargeTime(chargeTime);
        setAttackDamage(damage);
        setTwoHanded(twoHanded);
        setProjectileAmount(projectileAmount);
        setDisableIFrames(disableIFrames);
        setProjectileVelocity(projectileVelocity);
        setInaccuracy(inaccuracy);
        setFovModifier(fovModifier);
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

        for (int i = 0; i < 45; i++) {
            double offsetX = random.nextDouble() * 1 - 0.3;
            double offsetY = random.nextDouble() * 1 - 0.3;
            double offsetZ = random.nextDouble() * 1 - 0.3;

            level.addParticle(ParticleTypes.SMOKE, spawnX + offsetX, spawnY + offsetY, spawnZ + offsetZ, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void shoot(LivingEntity entity, Level level) {
        ItemStack itemStack = null;

        if (entity.getMainHandItem().getItem() instanceof GunItem) {
            itemStack = entity.getMainHandItem();
        } else {
            itemStack = entity.getOffhandItem();
        }

        for (int i = 0; i < projectileAmount; i++) {
            RoundBallProjectile projectile = new RoundBallProjectile(ModEntities.ROUND_BALL_PROJECTILE.get(), level);

            projectile.setDamage(getAttackDamage(itemStack));
            projectile.setNoGravity(true);
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        ItemStack ammo = getProjectile(pPlayer, itemStack);
        ItemStack gunpowder = getGunpowder(pPlayer, itemStack);

        if (isCharged(itemStack) && (!isTwoHanded() || itemStack == pPlayer.getMainHandItem())) {
            setCharged(itemStack, false);
            clearChargedProjectiles(itemStack);
            itemStack.hurtAndBreak(1, pPlayer, (p) -> p.broadcastBreakEvent(pPlayer.getUsedItemHand()));
            shoot(pPlayer, pLevel);
            if (pLevel instanceof ServerLevel serverLevel) {
                triggerAnim(pPlayer, GeoItem.getOrAssignId(pPlayer.getItemInHand(pUsedHand), serverLevel), "Controller", "shoot");
            }
            pPlayer.stopUsingItem();
        } else if (((ammo != null && !ammo.isEmpty()) && (gunpowder != null && !gunpowder.isEmpty())) || pPlayer.getAbilities().instabuild) {
            if (ammo == null || ammo.isEmpty()) { ammo = new ItemStack(ModItems.ROUND_BALL.get()); gunpowder = new ItemStack(Items.GUNPOWDER); }

            if (!isTwoHanded() || itemStack == pPlayer.getMainHandItem()){
                if (itemStack == pPlayer.getMainHandItem() || !(pPlayer.getMainHandItem().getItem() instanceof GunItem)) {
                    pPlayer.startUsingItem(pUsedHand);
                    setCharging(itemStack, true);
                    if (pLevel instanceof ServerLevel serverLevel) {
                        triggerAnim(pPlayer, GeoItem.getOrAssignId(pPlayer.getItemInHand(pUsedHand), serverLevel), "Controller", "cock");
                    }
                }
            }
        }


        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        int i = this.getUseDuration(pStack) - pTimeCharged;

        ItemStack ammo = getProjectile((Player)pLivingEntity, pStack);
        ItemStack gunpowder = getGunpowder((Player)pLivingEntity, pStack);

        if (i >= getChargeTime(pStack) && !isCharged(pStack)){
            if (((ammo != null && !ammo.isEmpty()) && (gunpowder != null && !gunpowder.isEmpty())) || ((Player)pLivingEntity).getAbilities().instabuild) {
                if (ammo == null || ammo.isEmpty()){ ammo = new ItemStack(ModItems.ROUND_BALL.get()); gunpowder = new ItemStack(Items.GUNPOWDER); }

                setCharged(pStack, true);
                if (!((Player) pLivingEntity).getAbilities().instabuild) {
                    ammo.shrink(1);
                    gunpowder.shrink(1);
                }

                addChargedProjectile(pStack, ammo);
            }
        } else if (!isCharged(pStack)) {
            if (pLevel instanceof ServerLevel serverLevel) {
                triggerAnim(pLivingEntity, GeoItem.getOrAssignId(pStack, serverLevel), "Controller", "uncocked");
            }
        }
        setCharging(pStack, false);
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        float f = (float)(pStack.getUseDuration() - pRemainingUseDuration);

        if (f == getChargeTime(pStack) - 1 && !isCharged(pStack)) {
            pLevel.playSound(pLivingEntity, pLivingEntity.blockPosition(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1, 1);
            if (pLevel instanceof ServerLevel serverLevel) {
                triggerAnim(pLivingEntity, GeoItem.getOrAssignId(pStack, serverLevel), "Controller", "cocked");
            }
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    public void setProjectileAmount(int projectileAmount) { this.projectileAmount = projectileAmount; }
    public int getProjectileAmount() { return this.projectileAmount; }

    public void setDisableIFrames(boolean disableIFrames) { this.disableIFrames = disableIFrames; }
    public boolean DisabledIFrames() { return disableIFrames; }

}
