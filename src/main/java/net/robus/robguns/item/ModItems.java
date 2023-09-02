package net.robus.robguns.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.GeoGunItems.FlintlockItem;
import net.robus.robguns.item.GeoGunItems.MusketItem;

public class ModItems {
    public static DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RobGuns.MODID);

    public static final RegistryObject<Item> FLINTLOCK = ITEMS.register("flintlock",
            () -> new FlintlockItem(new Item.Properties().stacksTo(1).durability(251), 20, 8));

    public static final RegistryObject<Item> MUSKET = ITEMS.register("musket",
            () -> new MusketItem(new Item.Properties().stacksTo(1).durability(327), 30, 15));

    public static final RegistryObject<Item> ROUND_BALL = ITEMS.register("round_ball",
            () -> new RoundBallItem(new Item.Properties().stacksTo(64)));

    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}
