package net.robus.robguns.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.robus.robguns.RobGuns;
import net.robus.robguns.client.ArmPoses;
import net.robus.robguns.client.gui.ModGui;
import net.robus.robguns.item.mod_items.GunItem;
import net.robus.robguns.item.mod_items.custom_items.ScopedMusketItem;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = RobGuns.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
            LocalPlayer player = Minecraft.getInstance().player;

            if(player == null)
                return;

            if(player.getMainHandItem().getItem() instanceof ScopedMusketItem item && item.isZooming())
                event.setCanceled(true);

            if(player.getMainHandItem().getItem() instanceof GunItem && GunItem.isCharging(player.getMainHandItem()))
                event.setCanceled(true);
        }

        @SubscribeEvent
        public static void holdGunPost(RenderPlayerEvent event) {
            Player player = event.getEntity();

            if (player.getMainHandItem().getItem() instanceof GunItem gunItem) {
                if (gunItem.isTwoHanded()) {
                    if (GunItem.isCharged(player.getMainHandItem())) {
                        event.getRenderer().getModel().rightArmPose = ArmPoses.TWO_HAND_HOLD_GUN;
                        event.getRenderer().getModel().leftArmPose = ArmPoses.TWO_HAND_HOLD_GUN;
                    } else if (GunItem.isCharging(player.getUseItem())) {
                        event.getRenderer().getModel().rightArmPose = ArmPoses.FLINTLOCK_COCKING;
                        event.getRenderer().getModel().leftArmPose = ArmPoses.FLINTLOCK_COCKING;
                    }
                } else {
                    if (GunItem.isCharged(player.getMainHandItem())) {
                        event.getRenderer().getModel().rightArmPose = ArmPoses.ONE_HAND_HOLD_GUN;
                        event.getRenderer().getModel().leftArmPose = HumanoidModel.ArmPose.EMPTY;
                    } else if (GunItem.isCharging(player.getUseItem())) {
                        event.getRenderer().getModel().rightArmPose = ArmPoses.FLINTLOCK_COCKING;
                        event.getRenderer().getModel().leftArmPose = ArmPoses.FLINTLOCK_COCKING;
                    }
                }
            } else if (player.getOffhandItem().getItem() instanceof GunItem gunItem) {
                if (!gunItem.isTwoHanded()) {
                    if (GunItem.isCharged(player.getOffhandItem())) {
                        event.getRenderer().getModel().rightArmPose = HumanoidModel.ArmPose.EMPTY;
                        event.getRenderer().getModel().leftArmPose = ArmPoses.ONE_HAND_HOLD_GUN;
                    } else if (GunItem.isCharging(player.getUseItem())) {
                        event.getRenderer().getModel().rightArmPose = ArmPoses.FLINTLOCK_COCKING;
                        event.getRenderer().getModel().leftArmPose = ArmPoses.FLINTLOCK_COCKING;
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
            if (event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().getItem() instanceof GunItem gunItem &&
                    gunItem.isScoped()) {
                if (GunItem.isCharged(event.getPlayer().getUseItem())) {
                    float fovModifier = gunItem.getFovModifier();
                    event.setNewFovModifier(fovModifier);
                }
            }
        }

        @SubscribeEvent
        public static void onRenderGuiEvent(RenderGuiEvent event) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                if (player.isUsingItem() && player.getUseItem().getItem() instanceof GunItem gunItem &&
                        gunItem.isScoped()) {
                    if (GunItem.isCharged(player.getUseItem())) {
                        ModGui.renderScopedOverlay(event.getGuiGraphics());
                    }
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = RobGuns.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

    }
}
