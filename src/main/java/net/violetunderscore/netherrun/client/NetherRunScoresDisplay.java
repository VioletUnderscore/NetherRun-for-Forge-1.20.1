package net.violetunderscore.netherrun.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresProvider;

public class NetherRunScoresDisplay {
    private static final ResourceLocation NR_SCORE_BACKDROP = new ResourceLocation(NetherRun.MODID,
            "textures/gui/scores_display.png");
    private static final ResourceLocation NR_ICONS = new ResourceLocation(NetherRun.MODID,
            "textures/gui/role_icons.png");
    public static final IGuiOverlay HUD_NR_SCORE_DISPLAY = ((gui, guiGraphics, partialTick, width, height) -> {
        int x = width / 2;
        int y = 0;
        int targetScore = 0;

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level != null && minecraft.level.dimension().equals(Level.OVERWORLD)) {
            minecraft.level.getCapability(NetherRunScoresProvider.NETHERRUN_SCORES).ifPresent(scores -> {
                //scores.getScore(0);
                guiGraphics.blit(NR_SCORE_BACKDROP, x - 80, y, 0, 0, 160, 40, 160, 40);
                guiGraphics.blit(NR_ICONS, x - 75 , y + 2, 0 * 16, 7 * 16, 16, 16, 48, 256);
                guiGraphics.blit(NR_ICONS, x + 59,  y + 2, 2 * 16, 3 * 16, 16, 16, 48, 256);
                guiGraphics.drawString(Minecraft.getInstance().font, "00:00", x - 54, y + 10, 0xFF0089); //team 1 timer
                guiGraphics.drawString(Minecraft.getInstance().font, "00:00", x + 29, y + 10, 0x00FF00); //team 2 timer
                guiGraphics.drawString(Minecraft.getInstance().font, timeToString(scores.getScore(0)), x + 5, y + 27, 0xFFFFFF);  //target timer
                guiGraphics.drawString(Minecraft.getInstance().font, "Target:", x - 33, y + 27, 0xFFFFFF);
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, "Round 11", x, y + 10, 0xFFFFFF);
            });
        }

    });

    public static String timeToString(int time) {
        if (time > 118800) {
            return "99+";
        }
        return (""
                + (int)(Math.floor(time / 24000) - (Math.floor(time / 240000) * 10))
                + (int)(Math.floor(time / 2400) - (Math.floor(time / 24000) * 10))
                + ":"
                + (int)Math.floor((Math.floor(time / 40) - (Math.floor((time / 40) / 60) * 60)) / 10)
                + (int)((Math.floor(time / 40) - (Math.floor((time / 40) / 60) * 60)) - ((Math.floor((Math.floor(time / 40) - (Math.floor((time / 40) / 60) * 60)) / 10)) * 10))
        );
    }
}