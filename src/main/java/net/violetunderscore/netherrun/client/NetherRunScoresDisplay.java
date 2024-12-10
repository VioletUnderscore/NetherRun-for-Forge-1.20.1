package net.violetunderscore.netherrun.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.violetunderscore.netherrun.NetherRun;

public class NetherRunScoresDisplay {
    private static final ResourceLocation NR_SCORE_BACKDROP = new ResourceLocation(NetherRun.MODID,
            "textures/gui/scores_display.png");
    private static final ResourceLocation NR_ICONS = new ResourceLocation(NetherRun.MODID,
            "textures/gui/role_icons.png");
    public static final IGuiOverlay HUD_NR_SCORE_DISPLAY = ((gui, guiGraphics, partialTick, width, height) -> {
        int x = width / 2;
        int y = 0;

        guiGraphics.blit(NR_SCORE_BACKDROP, x - 80, y, 0, 0, 160, 40, 160, 40);
        guiGraphics.blit(NR_ICONS, x - 75 , y + 2, 0 * 16, 7 * 16, 16, 16, 48, 256);
        guiGraphics.blit(NR_ICONS, x + 59,  y + 2, 2 * 16, 3 * 16, 16, 16, 48, 256);
        guiGraphics.drawString(Minecraft.getInstance().font, "00:00", x - 54, y + 10, 0xFF0089);
        guiGraphics.drawString(Minecraft.getInstance().font, "00:00", x + 29, y + 10, 0x00FF00);
        guiGraphics.drawString(Minecraft.getInstance().font, "00:00", x + 5, y + 27, 0xFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, "Target:", x - 33, y + 27, 0xFFFFFF);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, "Round 11", x, y + 10, 0xFFFFFF);
    });
}