package net.robus.robguns.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.robus.robguns.item.mod_items.GunItem;
import net.robus.robguns.item.mod_items.custom_items.ScopedMusketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @Shadow
    public LocalPlayer player;

    @Redirect(method = "Lnet/minecraft/client/Minecraft;handleKeybinds()V", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I"))
    private void handleKeybinds(Inventory instance, int value)
    {
        if(player.getMainHandItem().getItem() instanceof ScopedMusketItem item && item.isZooming())
            return;
        if(player.getMainHandItem().getItem() instanceof GunItem && GunItem.isCharging(player.getMainHandItem()))
            return;
        instance.selected = value;
    }
}
