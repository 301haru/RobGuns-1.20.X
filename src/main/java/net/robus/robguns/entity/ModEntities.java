package net.robus.robguns.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.robus.robguns.RobGuns;
import net.robus.robguns.entity.mod_entities.GunProjectile;
import net.robus.robguns.entity.mod_entities.custom_entities.DragonsBreathProjectile;
import net.robus.robguns.entity.mod_entities.custom_entities.GubProjectile;
import net.robus.robguns.entity.mod_entities.custom_entities.RoundBallProjectile;

public class ModEntities {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RobGuns.MODID);

    public static final RegistryObject<EntityType<RoundBallProjectile>> ROUND_BALL_PROJECTILE =
            ENTITY_TYPES.register("round_ball_projectile",
            () -> EntityType.Builder.of(RoundBallProjectile::new, MobCategory.MISC)
                    .sized(0.4f, 0.4f).updateInterval(20).clientTrackingRange(6)
                    .build(RobGuns.MODID + ":round_ball_projectile"));

    public static final RegistryObject<EntityType<DragonsBreathProjectile>> DRAGONS_BREATH_PROJECTILE =
            ENTITY_TYPES.register("dragons_breath_projectile",
                    () -> EntityType.Builder.of(DragonsBreathProjectile::new, MobCategory.MISC)
                            .sized(0.625f, 0.625f).updateInterval(20).clientTrackingRange(6)
                            .build(RobGuns.MODID + ":dragons_breath_projectile"));

    public static final RegistryObject<EntityType<GubProjectile>> GUB_PROJECTILE =
            ENTITY_TYPES.register("gub_projectile",
                    () -> EntityType.Builder.<GubProjectile>of(GubProjectile::new, MobCategory.MISC)
                            .sized(1f, 1f).updateInterval(20).clientTrackingRange(6)
                            .build(RobGuns.MODID + ":gub_projectile"));

    public static void register(IEventBus eventBus) { ENTITY_TYPES.register(eventBus); }
}
