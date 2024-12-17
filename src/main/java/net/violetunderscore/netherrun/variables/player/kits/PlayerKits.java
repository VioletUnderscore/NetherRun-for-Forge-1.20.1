package net.violetunderscore.netherrun.variables.player.kits;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.violetunderscore.netherrun.block.ModBlocks;
import net.violetunderscore.netherrun.item.ItemFetcher;
import net.violetunderscore.netherrun.item.ModItems;

public class PlayerKits {
    private String[] playerKit = {
            "minecraft:white_concrete",
            "minecraft:wooden_sword",
            "minecraft:white_concrete",
            "netherrun:block_of_go_up",
            "minecraft:scaffolding",
            "netherrun:netherrun_pickaxe",
            "netherrun:go_up",
            "netherrun:squished_soul_slime_block",
            "netherrun:netherrun_boat",
            "netherrun:netherrun_totem"
    };

    private int warpx = 0;
    private int warpy = 0;
    private int warpz = 0;
    private int warptimer = 0;
    private boolean canwarp = false;
    private boolean keydown = false;
    private boolean iswarping = false;

    public Item getKitItem(int slot) {
        return ItemFetcher.getItem(playerKit[slot]);
    }

    public void setKit(String[] items) {
        this.playerKit = items;
    }

    public void addToKit(int slot, String item) {
        this.playerKit[slot] = item;
    }
    public void addToKit(int slot, Item item) {
        this.playerKit[slot] = ItemFetcher.ungetItem(item);
    }

    public void copyFrom(PlayerKits source) {
        this.playerKit = source.playerKit;
    }

    public void saveNBTData(CompoundTag nbt) {
        for (int v = 0; v < 10; v++) {
            nbt.putString("NRKit" + v, playerKit[v]);
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        for (int v = 0; v < 10; v++) {
            playerKit[v] = nbt.getString("NRKit" + v);
        }
    }



    public void setWarp(int x, int y, int z, int time) {
        warpx = x;
        warpy = y;
        warpz = z;
        warptimer = time;
    }
    public void setWarpTimer (int time) {
        warptimer = time;
    }
    public void setWarping (boolean v) {
        iswarping = v;
    }
    public void setCanWarp(boolean v) {
        canwarp = v;
    }
    public boolean getCanWarp() {
        return canwarp;
    }

    public int getWarpTime() {
        return warptimer;
    }
    public Vec3 getWarpSpace() {
        return new Vec3(warpx, warpy, warpz);
    }
    public boolean isWarping() {
        return iswarping;
    }
    public void setKeyDown(boolean v) {
        keydown = v;
    }
    public boolean getKeyDown() {
        return keydown;
    }
}
