package net.violetunderscore.netherrun.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.violetunderscore.netherrun.NetherRun;
import net.violetunderscore.netherrun.client.keybinds.NetherRunKeybinds;
import net.violetunderscore.netherrun.variables.colorEnums;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresData;
import net.violetunderscore.netherrun.variables.global.scores.NetherRunScoresDataManager;
import net.violetunderscore.netherrun.variables.player.kits.PlayerKitsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class NetherRunScoresDisplay {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation NR_SCORE_BACKDROP = new ResourceLocation(NetherRun.MODID,
            "textures/gui/scores_display.png");
    private static final ResourceLocation NR_ICONS = new ResourceLocation(NetherRun.MODID,
            "textures/gui/role_icons.png");
    private static final ResourceLocation NR_CD_SLOT = new ResourceLocation(NetherRun.MODID,
            "textures/gui/hotbar_cooldown.png");
    private static final ResourceLocation NR_CD_SLOT_SELECT = new ResourceLocation(NetherRun.MODID,
            "textures/gui/hotbar_selection_cooldown.png");
    public static final IGuiOverlay HUD_NR_SCORE_DISPLAY = ((gui, guiGraphics, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        Player player = Minecraft.getInstance().player;

        if (NetherRunGlobalClientData.isGameActive() && NetherRunGlobalClientData.getSpawnTimerR() != 0)
        {
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, ("Spawning the runner in " + (int)Math.ceil(((double)NetherRunGlobalClientData.getSpawnTimerR()) / 40) + " seconds!"), x, 50, 0xFFFFFF);
        } else if (NetherRunGlobalClientData.isGameActive() && NetherRunGlobalClientData.getSpawnTimerH() != 0)
        {
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, ("Spawning the hunter in " + (int)Math.ceil(((double)NetherRunGlobalClientData.getSpawnTimerH()) / 40) + " seconds!"), x, 50, 0xFFFFFF);
        }

        if (player != null) {
            /*Slot Related Stuff*/{
                boolean slotcd = false;
                int slot = 0;
                try {
                    if (player.getCooldowns().isOnCooldown(player.getInventory().offhand.get(0).getItem()) || !Objects.requireNonNull(player.getInventory().offhand.get(0).getTag()).getBoolean("netherrun.ready")) {
                        if (player.getCooldowns().isOnCooldown(player.getInventory().offhand.get(0).getItem())) {
                            guiGraphics.blit(NR_CD_SLOT, x - 121, y - 23, 0, 0, 24, 24, 24, 24);
                            guiGraphics.drawString(Minecraft.getInstance().font, ((int) Math.abs(Math.ceil(player.getCooldowns().getCooldownPercent(player.getInventory().offhand.get(0).getItem(), partialTick) * 100 - 100)) + "%"), x - 118, y - 32, 0xFFFFFF);
                        } else try {
                            if (player.getInventory().offhand.get(0).getTag().getLong("netherrun.ready_timeout") - player.level().getGameTime() > 0) {
                                guiGraphics.blit(NR_CD_SLOT, x - 121, y - 23, 0, 0, 24, 24, 24, 24);
                                guiGraphics.drawString(Minecraft.getInstance().font, ("" + ((int) Math.ceil((Objects.requireNonNull(player.getInventory().offhand.get(0).getTag()).getLong("netherrun.ready_timeout") - player.level().getGameTime()) / 20) + 1)), x - 118, y - 32, 0xFFFFFF);
                            }
                        } catch (NullPointerException e) {
                            guiGraphics.drawString(Minecraft.getInstance().font, "ERR", x - 118, y - 32, 0xFF0000);
                        }
                    }
                } catch (NullPointerException e) {

                }
                for (int v = 0; v < 9; v++) {
                    try {
                        if (player.getCooldowns().isOnCooldown(player.getInventory().getItem(v).getItem()) || !player.getInventory().getItem(v).getTag().getBoolean("netherrun.ready")) {
                            if (player.getInventory().selected == v) {
                                slotcd = true;
                                slot = v;
                            } else {
                                if (player.getCooldowns().isOnCooldown(player.getInventory().getItem(v).getItem())) {
                                    guiGraphics.blit(NR_CD_SLOT, x - 92 + (v * 20), y - 23, 0, 0, 24, 24, 24, 24);
                                    guiGraphics.drawString(Minecraft.getInstance().font, ((int) Math.abs(Math.ceil(player.getCooldowns().getCooldownPercent(player.getInventory().getItem(v).getItem(), partialTick) * 100 - 100)) + "%"), x - 89 + (v * 20), y - 32, 0xFFFFFF);
                                } else try {
                                    if (player.getInventory().getItem(v).getTag().getLong("netherrun.ready_timeout") - player.level().getGameTime() > 0) {
                                        guiGraphics.blit(NR_CD_SLOT, x - 92 + (v * 20), y - 23, 0, 0, 24, 24, 24, 24);
                                        guiGraphics.drawString(Minecraft.getInstance().font, ("" + ((int) Math.ceil((Objects.requireNonNull(player.getInventory().getItem(v).getTag()).getLong("netherrun.ready_timeout") - player.level().getGameTime()) / 20) + 1)), x - 89 + (v * 20), y - 32, 0xFFFFFF);
                                    }
                                } catch (NullPointerException e) {
                                    guiGraphics.drawString(Minecraft.getInstance().font, "ERR", x - 89 + (v * 20), y - 32, 0xFF0000);
                                }
                            }
                        }
                    } catch (NullPointerException e) {

                    }
                }
                if (slotcd) {
                    if (player.getCooldowns().isOnCooldown(player.getInventory().getItem(slot).getItem())) {
                        guiGraphics.blit(NR_CD_SLOT_SELECT, x - 92 + (slot * 20), y - 23, 0, 0, 24, 24, 24, 24);
                        guiGraphics.drawString(Minecraft.getInstance().font, ((int) Math.abs(Math.ceil(player.getCooldowns().getCooldownPercent(player.getInventory().getItem(slot).getItem(), partialTick) * 100 - 100)) + "%"), x - 89 + (slot * 20), y - 32, 0xFFFFFF);
                    } else try {
                        if (player.getInventory().getItem(slot).getTag().getLong("netherrun.ready_timeout") - player.level().getGameTime() > 0) {
                            guiGraphics.blit(NR_CD_SLOT_SELECT, x - 92 + (slot * 20), y - 23, 0, 0, 24, 24, 24, 24);
                            guiGraphics.drawString(Minecraft.getInstance().font, ("" + ((int) Math.ceil((Objects.requireNonNull(player.getInventory().getItem(slot).getTag()).getLong("netherrun.ready_timeout") - player.level().getGameTime()) / 20) + 1)), x - 89 + (slot * 20), y - 32, 0xFFFFFF);
                        }
                    } catch (NullPointerException e) {
                        guiGraphics.drawString(Minecraft.getInstance().font, "ERR", x - 89 + (slot * 20), y - 32, 0xFF0000);
                    }

                }
            }
        }

        /*Teleport Related Stuff*/{
            if (NetherRunGlobalClientData.isGameActive()) {
                int finalX = x;
                int finalY = y;
                try {
                    player.getCapability(PlayerKitsProvider.PLAYER_KITS).ifPresent(kit -> {
                        if (kit.isWarping()) {
                            guiGraphics.drawCenteredString(Minecraft.getInstance().font, ("Warping in " + (int) Math.ceil((double) kit.getWarpTime() / 40) + " seconds"), finalX, finalY - 45, 0xFFFFFF);
                        }
                        else if (kit.getCanWarp()) {
                            guiGraphics.drawCenteredString(Minecraft.getInstance().font, ("You're falling behind!"), finalX, finalY - 55, 0xFFFFFF);
                            guiGraphics.drawCenteredString(Minecraft.getInstance().font, ("Press [" + NetherRunKeybinds.NETHERRUN_TELEPORT.getTranslatedKeyMessage().getString() + "] to warp back to the runner"), finalX, finalY - 45, 0xFFFFFF);
                        }
                    });
                } catch (NullPointerException e) {
                    LOGGER.info("null while placing text");
                }
            }
        }

        /*Scoreboard Related Stuff*/{
            if (NetherRunGlobalClientData.isGameActive()) {
                x = 90;
                y = 0;

                guiGraphics.blit(NR_SCORE_BACKDROP, x - 80, y, 0, 0, 160, 40, 160, 40);
                if (player.getName().getString() == NetherRunGlobalClientData.getPlayer2Name())
                {
                    if (NetherRunGlobalClientData.getWhosTurn() == 2) {
                        guiGraphics.blit(NR_ICONS, x - 75, y + 2, 0 * 16, (NetherRunGlobalClientData.getColor2() - 1) * 16, 16, 16, 48, 256);
                        guiGraphics.blit(NR_ICONS, x + 59, y + 2, 2 * 16, (NetherRunGlobalClientData.getColor1() - 1) * 16, 16, 16, 48, 256);
                    } else {
                        guiGraphics.blit(NR_ICONS, x - 75, y + 2, 2 * 16, (NetherRunGlobalClientData.getColor2() - 1) * 16, 16, 16, 48, 256);
                        guiGraphics.blit(NR_ICONS, x + 59, y + 2, 0 * 16, (NetherRunGlobalClientData.getColor1() - 1) * 16, 16, 16, 48, 256);
                    }
                    guiGraphics.drawString(Minecraft.getInstance().font, timeToString(NetherRunGlobalClientData.getTeam2Score()), x - 54, y + 10, colorEnums.NetherRunHexColor(NetherRunGlobalClientData.getColor2())); //team 1 timer
                    guiGraphics.drawString(Minecraft.getInstance().font, timeToString(NetherRunGlobalClientData.getTeam1Score()), x + 29, y + 10, colorEnums.NetherRunHexColor(NetherRunGlobalClientData.getColor1())); //team 2 timer
                }
                else {
                    if (NetherRunGlobalClientData.getWhosTurn() == 2) {
                        guiGraphics.blit(NR_ICONS, x - 75, y + 2, 2 * 16, (NetherRunGlobalClientData.getColor1() - 1) * 16, 16, 16, 48, 256);
                        guiGraphics.blit(NR_ICONS, x + 59, y + 2, 0 * 16, (NetherRunGlobalClientData.getColor2() - 1) * 16, 16, 16, 48, 256);
                    } else {
                        guiGraphics.blit(NR_ICONS, x - 75, y + 2, 0 * 16, (NetherRunGlobalClientData.getColor1() - 1) * 16, 16, 16, 48, 256);
                        guiGraphics.blit(NR_ICONS, x + 59, y + 2, 2 * 16, (NetherRunGlobalClientData.getColor2() - 1) * 16, 16, 16, 48, 256);
                    }
                    guiGraphics.drawString(Minecraft.getInstance().font, timeToString(NetherRunGlobalClientData.getTeam1Score()), x - 54, y + 10, colorEnums.NetherRunHexColor(NetherRunGlobalClientData.getColor1())); //team 1 timer
                    guiGraphics.drawString(Minecraft.getInstance().font, timeToString(NetherRunGlobalClientData.getTeam2Score()), x + 29, y + 10, colorEnums.NetherRunHexColor(NetherRunGlobalClientData.getColor2())); //team 2 timer
                }
                guiGraphics.drawString(Minecraft.getInstance().font, timeToString(NetherRunGlobalClientData.getTargetScore()), x + 5, y + 27, 0xFFFFFF);  //target timer
                guiGraphics.drawString(Minecraft.getInstance().font, "Target:", x - 33, y + 27, 0xFFFFFF);
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, ("Round " + NetherRunGlobalClientData.getRound()), x, y + 10, 0xFFFFFF);
            }
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