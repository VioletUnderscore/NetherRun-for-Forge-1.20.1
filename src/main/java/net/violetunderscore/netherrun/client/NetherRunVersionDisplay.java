package net.violetunderscore.netherrun.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class NetherRunVersionDisplay {
    public static final IGuiOverlay HUD_NR_SCORE_DISPLAY = ((gui, guiGraphics, partialTick, width, height) -> {
        if (NetherRunGlobalClientData.isGameActive()) {
            int x = 0;
            int y = height;

            guiGraphics.drawString(Minecraft.getInstance().font, "Netherrun m7d13-1", x - 33, y + 27, 0xFFFFFF);
        }
    });
}
