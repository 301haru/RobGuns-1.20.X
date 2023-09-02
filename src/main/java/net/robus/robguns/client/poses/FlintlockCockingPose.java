package net.robus.robguns.client.poses;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.IArmPoseTransformer;
import net.robus.robguns.item.GunItem;

public class FlintlockCockingPose implements IArmPoseTransformer {

    @Override
    public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (arm.getId() == 1) {
            model.leftArm.yRot = 0.8F;
            model.leftArm.xRot = -0.97079635F;
            model.rightArm.yRot = -0.8F;
            model.rightArm.xRot = model.leftArm.xRot;
            float f = (float) ((GunItem)entity.getUseItem().getItem()).getChargeTime(entity.getUseItem().getItem().getDefaultInstance());
            float f1 = Mth.clamp((float)entity.getTicksUsingItem(), 0.0F, f);
            float f2 = f1 / f;
            model.leftArm.yRot = Mth.lerp(f2, 0.3F, 0.55F);
            model.leftArm.xRot = Mth.lerp(f2, model.leftArm.xRot, (-(float)Math.PI / 2.65F));
        } else {
            model.rightArm.yRot = -0.8F;
            model.rightArm.xRot = -0.97079635F;
            model.leftArm.yRot = 0.8F;
            model.leftArm.xRot = model.rightArm.xRot;
            float f = (float) ((GunItem)entity.getUseItem().getItem()).getChargeTime(entity.getUseItem().getItem().getDefaultInstance());
            float f1 = Mth.clamp((float)entity.getTicksUsingItem(), 0.0F, f);
            float f2 = f1 / f;
            model.rightArm.yRot = Mth.lerp(f2, 0.3F, 0.55F) * -1;
            model.rightArm.xRot = Mth.lerp(f2, model.rightArm.xRot, (-(float)Math.PI / 2.65F));
        }

    }
}
