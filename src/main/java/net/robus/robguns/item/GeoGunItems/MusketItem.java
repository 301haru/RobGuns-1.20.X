package net.robus.robguns.item.GeoGunItems;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.robus.robguns.item.GeoGunItem;
import net.robus.robguns.item.ModItems;
import net.robus.robguns.item.client.Musket.MusketRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.Random;
import java.util.function.Consumer;

public class MusketItem extends GeoGunItem {
    private static final Random random = new Random();

    public MusketItem(Properties pProperties, int chargeTime, float damage) {
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        ItemStack ammo = getProjectile(pPlayer, itemStack);
        ItemStack gunpowder = getGunpowder(pPlayer, itemStack);

        if (isCharged(itemStack) && (!isTwoHanded() || itemStack == pPlayer.getMainHandItem())) {
            setCharged(itemStack, false);
            shoot(pPlayer, pLevel);
            clearChargedProjectiles(itemStack);
            itemStack.hurtAndBreak(1, pPlayer, (p) -> p.broadcastBreakEvent(pPlayer.getUsedItemHand()));
            if (pLevel instanceof ServerLevel serverLevel) {
                triggerAnim(pPlayer, GeoItem.getOrAssignId(pPlayer.getItemInHand(pUsedHand), serverLevel), "Controller", "shoot");
            }
            pPlayer.stopUsingItem();
        } else if (((ammo != null && !ammo.isEmpty()) && (gunpowder != null && !gunpowder.isEmpty())) || pPlayer.getAbilities().instabuild) {
            if (ammo == null || ammo.isEmpty()) { ammo = new ItemStack(ModItems.ROUND_BALL.get()); gunpowder = new ItemStack(Items.GUNPOWDER); }

            if (!isTwoHanded() || itemStack == pPlayer.getMainHandItem()){
                pPlayer.startUsingItem(pUsedHand);
                setCharging(itemStack, true);
                if (pLevel instanceof ServerLevel serverLevel) {
                    triggerAnim(pPlayer, GeoItem.getOrAssignId(pPlayer.getItemInHand(pUsedHand), serverLevel), "Controller", "cock");
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

                pLevel.playSound(pLivingEntity, pLivingEntity.blockPosition(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1, 1);
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

        if (f >= getChargeTime(pStack) - 1) {
            if (pLevel instanceof ServerLevel serverLevel) {
                triggerAnim(pLivingEntity, GeoItem.getOrAssignId(pStack, serverLevel), "Controller", "cocked");
            }
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }
}
