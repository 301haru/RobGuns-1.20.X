package net.robus.robguns.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.robus.robguns.RobGuns;

public class ModEntities {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RobGuns.MODID);

    public static final RegistryObject<EntityType<RoundBallProjectile>> ROUND_BALL_PROJECTILE =
            ENTITY_TYPES.register("round_ball_projectile",
            () -> EntityType.Builder.of(RoundBallProjectile::new, MobCategory.MISC)
                    .sized(0.3125f, 0.3125f).setUpdateInterval(2).setTrackingRange(64).setShouldReceiveVelocityUpdates(true)
                    .build(RobGuns.MODID + ":round_ball_projectile"));

    public static void register(IEventBus eventBus) { ENTITY_TYPES.register(eventBus); }
}
