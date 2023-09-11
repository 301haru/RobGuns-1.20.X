package net.robus.robguns.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.mod_items.custom_items.*;
import net.robus.robguns.item.mod_items.RoundBallItem;

public class ModItems {
    public static DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RobGuns.MODID);

    public static final RegistryObject<Item> FLINTLOCK = ITEMS.register("flintlock",
            () -> new FlintlockItem(new Item.Properties().stacksTo(1).durability(251)));

    public static final RegistryObject<Item> MUSKET = ITEMS.register("musket",
            () -> new MusketItem(new Item.Properties().stacksTo(1).durability(327)));

    public static final RegistryObject<Item> SCOPED_MUSKET = ITEMS.register("scoped_musket",
            () -> new ScopedMusketItem(new Item.Properties().stacksTo(1).durability(251)));

    public static final RegistryObject<Item> BLUNDERBUSS = ITEMS.register("blunderbuss",
            () -> new BlunderbussItem(new Item.Properties().stacksTo(1).durability(327)));

    public static final RegistryObject<Item> DRAGONS_BREATH = ITEMS.register("dragons_breath",
            () -> new DragonsBreathItem(new Item.Properties().stacksTo(1).durability(251)));

    public static final RegistryObject<Item> ROUND_BALL = ITEMS.register("round_ball",
            () -> new RoundBallItem(new Item.Properties().stacksTo(64)));

    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}
