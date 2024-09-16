package com.shootforever.nuclear.module.modules.misc;

import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.setting.impl.BooleanSetting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;


public class Teams extends Module {
    private final BooleanSetting armorColor = new BooleanSetting("ArmorColor", this, true);

    public Teams() {
        super("Teams", Category.Misc);
        this.setEnabled(true);
    }

    public boolean isSameTeam(LivingEntity entity) {
        if (this.armorColor.getValue() && entity instanceof Player entityPlayer) {
            ItemStack myHead = (ItemStack)mc.player.getInventory().armor.get(3);
            ItemStack entityHead = (ItemStack)entityPlayer.getInventory().armor.get(3);
            if (!myHead.isEmpty() && !entityHead.isEmpty() && myHead.getItem() instanceof ArmorItem && entityHead.getItem() instanceof ArmorItem) {
                return this.getArmorColor(myHead) == this.getArmorColor(entityHead);
            }
        }

        return false;
    }

    private int getArmorColor(ItemStack stack) {
        return stack.getItem() instanceof DyeableLeatherItem ? ((DyeableLeatherItem)stack.getItem()).getColor(stack) : -1;
    }
}
