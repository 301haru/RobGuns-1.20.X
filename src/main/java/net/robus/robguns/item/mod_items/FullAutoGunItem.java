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
import net.robus.robguns.item.ModItems;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.Random;

public class FullAutoGunItem extends GeoGunItem {

    private static final Random random = new Random();

    public FullAutoGunItem(Properties pProperties, float damage, int chargeTime, float projectileVelocity, boolean twoHanded, boolean scoped,
                           float inaccuracy, float fovModifier) {
        super(pProperties);
        setAttackDamage(damage);
        setChargeTime(chargeTime);
        setProjectileVelocity(projectileVelocity);
        setTwoHanded(twoHanded);
        setScoped(scoped);
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        ItemStack ammo = getProjectile(pPlayer, itemStack);
        ItemStack gunpowder = getGunpowder(pPlayer, itemStack);

        if (isScoped()) {
            if (isCharged(itemStack) && (!isTwoHanded() || itemStack == pPlayer.getMainHandItem())) {
                pPlayer.startUsingItem(pUsedHand);
                pLevel.playSound(pPlayer, pPlayer.blockPosition(), SoundEvents.SPYGLASS_USE, SoundSource.PLAYERS, 1, 1);

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
        } else {
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
                if (ammo == null || ammo.isEmpty()) {
                    ammo = new ItemStack(ModItems.ROUND_BALL.get());
                    gunpowder = new ItemStack(Items.GUNPOWDER);
                }

                if (!isTwoHanded() || itemStack == pPlayer.getMainHandItem()) {
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

        if (isScoped()) {
            if (isCharged(pStack) && pStack == pLivingEntity.getMainHandItem()) {
                setCharged(pStack, false);
                clearChargedProjectiles(pStack);
                pStack.hurtAndBreak(1, pLivingEntity, (p) -> p.broadcastBreakEvent(pLivingEntity.getUsedItemHand()));
                shoot(pLivingEntity, pLevel);
                if (pLevel instanceof ServerLevel serverLevel) {
                    triggerAnim(pLivingEntity, GeoItem.getOrAssignId(pLivingEntity.getMainHandItem(), serverLevel), "Controller", "shoot");
                }
            } else if (i >= getChargeTime(pStack) && !isCharged(pStack)){
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
        } else {
            if (i >= getChargeTime(pStack) && !isCharged(pStack)) {
                if (((ammo != null && !ammo.isEmpty()) && (gunpowder != null && !gunpowder.isEmpty())) || ((Player) pLivingEntity).getAbilities().instabuild) {
                    if (ammo == null || ammo.isEmpty()) {
                        ammo = new ItemStack(ModItems.ROUND_BALL.get());
                        gunpowder = new ItemStack(Items.GUNPOWDER);
                    }

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

}
