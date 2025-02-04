package com.shootforever.nuclear.util;

import com.google.common.collect.Multimap;
import com.shootforever.nuclear.Nuclear;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class InventoryUtil {
    private InventoryUtil() {
        throw new AssertionError();
    }

    public static boolean isBestCrossBow(@NotNull InventoryMenu handler, @NotNull ItemStack itemStack) {
        double bestBowDmg = -1.0;
        ItemStack bestBow = ItemStack.EMPTY;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = handler.getSlot(i).getItem();
            if (!stack.isEmpty() && stack.getItem() instanceof CrossbowItem) {
                double damage = getCrossBowDamage(stack);
                if (damage > bestBowDmg) {
                    bestBow = stack;
                    bestBowDmg = damage;
                }
            }
        }

        return itemStack.equals(bestBow) || getBowDamage(itemStack) > bestBowDmg;
    }

    public static boolean isBestBow(@NotNull AbstractContainerMenu handler, @NotNull ItemStack itemStack) {
        double bestBowDmg = -1.0;
        ItemStack bestBow = ItemStack.EMPTY;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = handler.getSlot(i).getItem();
            if (!stack.isEmpty() && stack.getItem() instanceof BowItem) {
                double damage = getBowDamage(stack);
                if (damage > bestBowDmg) {
                    bestBow = stack;
                    bestBowDmg = damage;
                }
            }
        }

        return itemStack.equals(bestBow) || getBowDamage(itemStack) > bestBowDmg;
    }

    public static double getCrossBowDamage(@NotNull ItemStack stack) {
        double damage = 0.0;
        if (stack.getItem() instanceof CrossbowItem && stack.hasFoil()) {
            damage += getLevel(Enchantments.POWER_ARROWS, stack);
        }

        return damage;
    }

    public static double getBowDamage(@NotNull ItemStack stack) {
        double damage = 0.0;
        if (stack.getItem() instanceof BowItem && stack.hasFoil()) {
            damage += getLevel(Enchantments.POWER_ARROWS, stack);
        }

        return damage;
    }

    public static boolean isBuffPotion(@NotNull ItemStack stack) {
        for (MobEffectInstance effect : PotionUtils.getMobEffects(stack)) {
            if (!effect.getEffect().isBeneficial()) {
                return false;
            }
        }

        return true;
    }

    public static boolean isBestTool(@NotNull AbstractContainerMenu handler, @NotNull ItemStack itemStack) {
        int type = getToolType(itemStack);
        Tool bestTool = new Tool(-1, -1.0, ItemStack.EMPTY);

        for (int i = 9; i < 45; i++) {
            ItemStack stack = handler.getSlot(i).getItem();
            if (!stack.isEmpty() && stack.getItem() instanceof DiggerItem && type == getToolType(stack)) {
                double efficiency = getToolScore(stack);
                if (efficiency > (double) getToolScore(bestTool.getItem())) {
                    bestTool = new Tool(i, efficiency, stack);
                }
            }
        }

        return bestTool.getItem().equals(itemStack) || getToolScore(itemStack) > getToolScore(bestTool.getItem());
    }

    public static float getToolScore(@NotNull ItemStack stack) {
        float score = 0.0F;
        Item item = stack.getItem();
        if (item instanceof DiggerItem tool) {
            if (item instanceof PickaxeItem) {
                score = tool.getDestroySpeed(stack, Blocks.STONE.defaultBlockState()) - 0.0F;
            } else {
                if (!(item instanceof AxeItem)) {
                    return 1.0F;
                }

                score = tool.getDestroySpeed(stack, Blocks.DARK_OAK_LOG.defaultBlockState());
            }

            score += (float) getLevel(Enchantments.BLOCK_EFFICIENCY, stack) * 0.0075F;
            score += (float) getLevel(Enchantments.BLOCK_EFFICIENCY, stack) / 100.0F;
            score += (float) getLevel(Enchantments.SHARPNESS, stack);
        }

        return score;
    }

    public static float getToolEfficiency(@NotNull ItemStack itemStack) {
        DiggerItem tool = (DiggerItem) itemStack.getItem();
        float efficiency = tool.getTier().getSpeed();
        int lvl = getLevel(Enchantments.BLOCK_EFFICIENCY, itemStack);
        if (efficiency > 1.0F && lvl > 0) {
            efficiency += (float) (lvl * lvl + 1);
        }

        return efficiency;
    }

    public static boolean isGoodFood(@NotNull ItemStack stack) {
        if (stack.getItem() == Items.GOLDEN_APPLE) {
            return true;
        } else {
            FoodProperties component = stack.getItem().getFoodProperties();
            if (component == null) return false;
            return component.getNutrition() >= 4 && component.getSaturationModifier() >= 0.3F;
        }
    }

    public static boolean isGoodItem(@NotNull ItemStack stack) {
        if (Nuclear.mc.player == null) return false;

        Item item = stack.getItem();
        return item == Items.FIRE_CHARGE
                || item == Items.ENDER_PEARL
                || item == Items.ARROW
                || item == Items.WATER_BUCKET
                || item == Items.SLIME_BALL
                || item == Items.TNT
                || item instanceof CrossbowItem && isBestCrossBow(Nuclear.mc.player.inventoryMenu, stack);
    }

    public static boolean isBestSword(@NotNull AbstractContainerMenu c, @NotNull ItemStack itemStack) {
        double damage = 0.0;
        ItemStack bestStack = ItemStack.EMPTY;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = c.getSlot(i).getItem();
            if (!stack.isEmpty() && stack.getItem() instanceof SwordItem) {
                double newDamage = getItemDamage(stack);
                if (newDamage > damage) {
                    damage = newDamage;
                    bestStack = stack;
                }
            }
        }

        return bestStack.equals(itemStack) || getItemDamage(itemStack) > damage;
    }

    public static boolean isBestArmor(@NotNull AbstractContainerMenu c, @NotNull ItemStack itemStack) {
        ArmorItem itemArmor = (ArmorItem) itemStack.getItem();
        double reduction = 0.0;
        ItemStack bestStack = ItemStack.EMPTY;

        for (int i = 5; i < 45; i++) {
            ItemStack stack = c.getSlot(i).getItem();
            if (!stack.isEmpty()) {
                Item newReduction = stack.getItem();
                if (newReduction instanceof ArmorItem stackArmor) {
                    if (stackArmor.getSlot().getFilterFlag() - 1 == itemArmor.getSlot().getFilterFlag() - 1) {
                        double newReductionX = getDamageReduction(stack);
                        if (newReductionX > reduction) {
                            reduction = newReductionX;
                            bestStack = stack;
                        }
                    }
                }
            }
        }

        return bestStack.equals(itemStack) || getDamageReduction(itemStack) > reduction;
    }

    public static double getDamageReduction(@NotNull ItemStack stack) {
        double reduction = 0.0;
        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return 0.0;
        } else {
            reduction += armor.getDefense();
            if (stack.hasFoil()) {
                reduction += (double) getLevel(Enchantments.ALL_DAMAGE_PROTECTION, stack) * 0.25;
            }

            return reduction;
        }
    }

    public static double getItemDamage(@NotNull ItemStack stack) {
        double damage = 0.0;
        Multimap<Attribute, AttributeModifier> attributeModifierMap = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);

        for (Attribute attributeName : attributeModifierMap.keySet()) {
            if (attributeName.getDescriptionId().equals("attribute.name.generic.attack_damage")) {
                Iterator<AttributeModifier> attributeModifiers = attributeModifierMap.get(attributeName).iterator();
                if (attributeModifiers.hasNext()) {
                    damage += attributeModifiers.next().getAmount();
                }
                break;
            }
        }

        if (stack.hasFoil()) {
            damage += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
            damage += (double) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, stack) * 1.25;
        }

        return damage;
    }

    private static int getLevel(@NotNull Enchantment registryKey, @NotNull ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(registryKey, itemStack);
    }

    public static int getToolType(@NotNull ItemStack stack) {
        DiggerItem tool = (DiggerItem) stack.getItem();
        if (tool instanceof PickaxeItem) {
            return 0;
        } else {
            return tool instanceof AxeItem ? 1 : -1;
        }
    }

    public static @NotNull List<ItemStack> getItemStacks(@NotNull Player player) {
        List<ItemStack> result = new ArrayList<>();

        for (Slot slot : player.inventoryMenu.slots) {
            if (!slot.getItem().isEmpty()) {
                result.add(slot.getItem());
            }
        }

        return result;
    }

    public static float getPlayerArmorScore(@NotNull Player player) {
        float score = 0.0F;

        for (int armorSlot = 5; armorSlot < 9; armorSlot++) {
            ItemStack stack = player.inventoryMenu.getSlot(armorSlot).getItem();
            score += (float) getDamageReduction(stack);
        }

        return score;
    }

    public static boolean isArmorBetter(@NotNull Player player) {
        if (Nuclear.mc.player == null) return false;
        return getPlayerArmorScore(player) < getPlayerArmorScore(Nuclear.mc.player);
    }

    public enum BlockAction {
        PLACE,
        REPLACE,
        PLACE_ON
    }

    private record Tool(int slot, double efficiency, @NotNull ItemStack stack) {
        public ItemStack getItem() {
            return this.stack;
        }
    }
}
