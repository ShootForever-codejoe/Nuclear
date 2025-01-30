package com.shootforever.nuclear.module.modules.misc;

import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.values.BooleanValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Team extends Module {
    private final BooleanValue armorColor = new BooleanValue(this, "ArmorColor", true);

    public Team() {
        super("Team", Category.MISC);
    }

    public boolean isSameTeam(@NotNull LivingEntity entity) {
        if (mc.player == null || !isEnabled()) return false;

        if (armorColor.getValue() && entity instanceof Player entityPlayer) {
            ItemStack myHead = mc.player.getInventory().armor.get(3);
            ItemStack entityHead = entityPlayer.getInventory().armor.get(3);
            if (!myHead.isEmpty() && !entityHead.isEmpty() && myHead.getItem() instanceof ArmorItem && entityHead.getItem() instanceof ArmorItem) {
                return getArmorColor(myHead) == getArmorColor(entityHead);
            }
        }

        return false;
    }

    private int getArmorColor(@NotNull ItemStack stack) {
        return stack.getItem() instanceof DyeableLeatherItem ? ((DyeableLeatherItem)stack.getItem()).getColor(stack) : -1;
    }
}
