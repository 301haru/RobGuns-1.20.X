package net.robus.robguns.client;

import net.minecraft.client.model.HumanoidModel;
import net.robus.robguns.client.poses.FlintlockCockingPose;
import net.robus.robguns.client.poses.OneHandedGunArmPose;
import net.robus.robguns.client.poses.TwoHandedGunArmPose;

public class ArmPoses{

    public static final  HumanoidModel.ArmPose ONE_HAND_HOLD_GUN = HumanoidModel.ArmPose.create("robguns:one_hand_hold_gun",
            false, new OneHandedGunArmPose());

    public static final  HumanoidModel.ArmPose TWO_HAND_HOLD_GUN = HumanoidModel.ArmPose.create("robguns:two_hand_hold_gun",
            true, new TwoHandedGunArmPose());

    public static final HumanoidModel.ArmPose FLINTLOCK_COCKING = HumanoidModel.ArmPose.create("robguns:flintlock_cocking",
            true, new FlintlockCockingPose());

    public static void init() {

    }
}
