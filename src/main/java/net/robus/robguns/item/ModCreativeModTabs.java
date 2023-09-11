package net.robus.robguns.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.robus.robguns.RobGuns;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RobGuns.MODID);

    public static final RegistryObject<CreativeModeTab> ROB_GUNS_TAB = CREATIVE_MODE_TABS.register("rob_guns_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MUSKET.get()))
                    .title(Component.translatable("creativetab.rob_guns_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.MUSKET.get());
                        pOutput.accept(ModItems.FLINTLOCK.get());
                        pOutput.accept(ModItems.SCOPED_MUSKET.get());
                        pOutput.accept(ModItems.BLUNDERBUSS.get());
                        pOutput.accept(ModItems.DRAGONS_BREATH.get());
                        pOutput.accept(ModItems.ROUND_BALL.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
