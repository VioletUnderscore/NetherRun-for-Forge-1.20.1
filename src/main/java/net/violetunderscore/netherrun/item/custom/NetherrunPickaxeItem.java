package net.violetunderscore.netherrun.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.violetunderscore.netherrun.item.ModItems;

import java.util.Map;

//sorry this is so hard to read maybe ill come fix it at some point lol :p

public class NetherrunPickaxeItem extends PickaxeItem {

    public NetherrunPickaxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pStack.getOrCreateTag().getLong("netherrun.timeout") <= pLevel.getGameTime()
                && pStack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) == 10)
        {
            Map<Enchantment, Integer> pEnchantments = EnchantmentHelper.getEnchantments(pStack);

            pEnchantments.remove(Enchantments.BLOCK_EFFICIENCY);
            EnchantmentHelper.setEnchantments(pEnchantments, pStack);
        }
        else if (pStack.getOrCreateTag().getLong("netherrun.timeout") > pLevel.getGameTime()
                && pStack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) != 10)
        {
            pStack.enchant(Enchantments.BLOCK_EFFICIENCY, 10);
        }
        if (pStack.getOrCreateTag().getLong("netherrun.ready_timeout") <= pLevel.getGameTime()) {
            pStack.getOrCreateTag().putBoolean("netherrun.ready", true);
            pStack.getOrCreateTag().putInt("CustomModelData", 0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide() && pPlayer.getMainHandItem().getTag().getLong("netherrun.ready_timeout") <= pLevel.getGameTime()
                && pPlayer.getMainHandItem().getItem().toString().equals(ModItems.NETHERRUN_PICKAXE.get().toString()))
        {
            pPlayer.getMainHandItem().getTag().putBoolean("netherrun.ready", false);
            pPlayer.getMainHandItem().getTag().putInt("CustomModelData", 1);
            pPlayer.getMainHandItem().getTag().putLong("netherrun.timeout", pLevel.getGameTime() + 100);
            pPlayer.getMainHandItem().getTag().putLong("netherrun.ready_timeout", pLevel.getGameTime() + 30 * 20);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
