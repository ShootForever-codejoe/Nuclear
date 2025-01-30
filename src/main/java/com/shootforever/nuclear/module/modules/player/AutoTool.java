package com.shootforever.nuclear.module.modules.player;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.Render2DEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public class AutoTool extends Module {
    private int prevItem = 0;
    private boolean mining = false;

    public AutoTool() {
        super("AutoTool", Category.PLAYER);
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (mc.player == null) return;

        if (!mc.options.keyUse.isDown() && mc.options.keyAttack.isDown() && mc.hitResult != null && mc.hitResult.getType() == Type.BLOCK) {
            if (!mining) {
                prevItem = mc.player.getInventory().selected;
            }

            switchSlot();
            mining = true;
        } else if (mining) {
            restore();
            mining = false;
        } else {
            prevItem = mc.player.getInventory().selected;
        }
    }

    public void switchSlot() {
        if (mc.player == null || mc.level == null) return;

        float bestSpeed = 1.0F;
        int bestSlot = -1;
        if (mc.hitResult != null && !mc.level.isEmptyBlock(((BlockHitResult) mc.hitResult).getBlockPos())) {
            BlockState blockState = mc.level.getBlockState(((BlockHitResult) mc.hitResult).getBlockPos());

            for (int i = 0; i <= 8; i++) {
                ItemStack item = mc.player.getInventory().getItem(i);
                if (!item.isEmpty()) {
                    float speed = item.getDestroySpeed(blockState);
                    if (speed > bestSpeed) {
                        bestSpeed = speed;
                        bestSlot = i;
                    }
                }
            }

            if (bestSlot != -1) {
                mc.player.getInventory().selected = bestSlot;
            }
        }
    }

    public void restore() {
        if (mc.player == null || mc.gameMode == null) return;

        for (int i = 0; i <= 8; i++) {
            if (i == prevItem) {
                mc.player.getInventory().selected = i;
                mc.gameMode.tick();
            }
        }
    }

    @Override
    protected void onEnable() {
        prevItem = 0;
    }
}
