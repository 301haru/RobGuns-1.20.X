package net.robus.robguns.client.poses;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.IArmPoseTransformer;

public class OneHandedGunArmPose implements IArmPoseTransformer {

    @Override
    public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (arm.getId() == 0) {
            model.leftArm.yRot = -0.1F + model.head.yRot;
            model.leftArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot;
        } else {
            model.rightArm.yRot = -0.1F + model.head.yRot;
            model.rightArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot;
        }
    }

}
