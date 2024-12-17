package net.violetunderscore.netherrun.variables.player.kits;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
}
