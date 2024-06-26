package net.robus.robguns;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.robus.robguns.client.ArmPoses;
import net.robus.robguns.entity.ModEntities;
import net.robus.robguns.entity.mod_entities.custom_entities.DragonsBreathProjectile;
import net.robus.robguns.entity.mod_entities.custom_entities.GubProjectile;
import net.robus.robguns.entity.mod_entities.custom_entities.RoundBallProjectile;
import net.robus.robguns.item.ModCreativeModTabs;
import net.robus.robguns.item.ModItems;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(RobGuns.MODID)
public class RobGuns {
    public static final String MODID = "robguns";

    public RobGuns() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.ROUND_BALL_PROJECTILE.get(), (context) -> new ThrownItemRenderer<RoundBallProjectile>(context));
            EntityRenderers.register(ModEntities.DRAGONS_BREATH_PROJECTILE.get(), (context) -> new ThrownItemRenderer<DragonsBreathProjectile>(context));
            EntityRenderers.register(ModEntities.GUB_PROJECTILE.get(), (context) -> new ThrownItemRenderer<GubProjectile>(context));
            ArmPoses.init();
        }
    }
}
