package net.robus.robguns.item;

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
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GeoGunItem extends GunItem implements GeoItem {
    private static final RawAnimation COCK_ANIM = RawAnimation.begin().then("cock", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SHOOT_ANIM = RawAnimation.begin().then("fire", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation COCKED_ANIM = RawAnimation.begin().then("cocked", Animation.LoopType.LOOP);
    private static final RawAnimation UNCOCKED_ANIM = RawAnimation.begin().then("uncocked", Animation.LoopType.LOOP);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public GeoGunItem(Properties pProperties) {
        super(pProperties);
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Controller", 0, state -> PlayState.STOP)
                .triggerableAnim("shoot", SHOOT_ANIM)
                .triggerableAnim("uncocked", UNCOCKED_ANIM)
                .triggerableAnim("cock", COCK_ANIM).setAnimationSpeed(1f / (getChargeTime(this.getDefaultInstance()) / 20f))
                .triggerableAnim("cocked", COCKED_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
