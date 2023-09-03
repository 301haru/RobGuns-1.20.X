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
