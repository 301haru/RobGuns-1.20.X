package net.robus.robguns.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.robus.robguns.RobGuns;
import net.robus.robguns.client.ArmPoses;
import net.robus.robguns.item.GeoGunItems.FlintlockItem;
import net.robus.robguns.item.GeoGunItems.MusketItem;
import net.robus.robguns.item.GunItem;

@Mod.EventBusSubscriber(modid = RobGuns.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerGunPoseHandler {

    @SubscribeEvent
    public static void holdGunPost(RenderPlayerEvent event)
    {
        Player player = event.getEntity();

        if(player.getMainHandItem().getItem() instanceof FlintlockItem || player.getOffhandItem().getItem() instanceof FlintlockItem) {
            if (GunItem.isCharged(player.getMainHandItem())) {
                event.getRenderer().getModel().rightArmPose = ArmPoses.ONE_HAND_HOLD_GUN;
                event.getRenderer().getModel().leftArmPose = HumanoidModel.ArmPose.EMPTY;
            } else if (GunItem.isCharged(player.getOffhandItem())) {
                event.getRenderer().getModel().rightArmPose = HumanoidModel.ArmPose.EMPTY;
                event.getRenderer().getModel().leftArmPose = ArmPoses.ONE_HAND_HOLD_GUN;
            } else if (GunItem.isCharging(player.getUseItem())) {
                event.getRenderer().getModel().rightArmPose = ArmPoses.FLINTLOCK_COCKING;
                event.getRenderer().getModel().leftArmPose = ArmPoses.FLINTLOCK_COCKING;
            }
        } else if (player.getMainHandItem().getItem() instanceof MusketItem || player.getOffhandItem().getItem() instanceof MusketItem) {
            if (GunItem.isCharged(player.getMainHandItem()) || GunItem.isCharged(player.getOffhandItem())) {
                event.getRenderer().getModel().rightArmPose = ArmPoses.TWO_HAND_HOLD_GUN;
                event.getRenderer().getModel().leftArmPose = ArmPoses.TWO_HAND_HOLD_GUN;
            } else if (GunItem.isCharging(player.getUseItem())) {
                event.getRenderer().getModel().rightArmPose = ArmPoses.FLINTLOCK_COCKING;
                event.getRenderer().getModel().leftArmPose = ArmPoses.FLINTLOCK_COCKING;
            }
        }
    }

}
