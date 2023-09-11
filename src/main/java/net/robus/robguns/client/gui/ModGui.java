package net.robus.robguns.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.robus.robguns.RobGuns;
import net.robus.robguns.item.mod_items.GunItem;

public class ModGui extends Gui {
    private static final ResourceLocation SCOPED_LOCATION = new ResourceLocation(RobGuns.MODID, "textures/misc/robguns_scoped_texture.png");
    protected static float scopeScale;

    public ModGui(Minecraft pMinecraft, ItemRenderer pItemRenderer) {
        super(pMinecraft, pItemRenderer);
    }

    public static void render(GuiGraphics pGuiGraphics) {

    }

    public static void renderScopedOverlay(GuiGraphics pGuiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        float z = minecraft.getDeltaFrameTime();
        RenderSystem.enableBlend();

        scopeScale = Mth.lerp(0.5F * z, scopeScale, 1.125F);
        if (minecraft.options.getCameraType().isFirstPerson()) {
            if (((GunItem)minecraft.player.getUseItem().getItem()).isScoped() && GunItem.isCharged(minecraft.player.getUseItem())) {
                int screenWidth = pGuiGraphics.guiWidth();
                int screenHeight = pGuiGraphics.guiHeight();

                float f = (float)Math.min(screenWidth, screenHeight);
                float f1 = Math.min((float)screenWidth / f, (float)screenHeight / f) * scopeScale;
                int i = Mth.floor(f * f1);
                int j = Mth.floor(f * f1);
                int k = (screenWidth - i) / 2;
                int l = (screenHeight - j) / 2;
                int i1 = k + i;
                int j1 = l + j;
                pGuiGraphics.blit(SCOPED_LOCATION, k, l, -90, 0.0F, 0.0F, i, j, i, j);
                pGuiGraphics.fill(RenderType.guiOverlay(), 0, j1, screenWidth, screenHeight, -90, -16777216);
                pGuiGraphics.fill(RenderType.guiOverlay(), 0, 0, screenWidth, l, -90, -16777216);
                pGuiGraphics.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
                pGuiGraphics.fill(RenderType.guiOverlay(), i1, l, screenWidth, j1, -90, -16777216);
            }
        }
    }
}
