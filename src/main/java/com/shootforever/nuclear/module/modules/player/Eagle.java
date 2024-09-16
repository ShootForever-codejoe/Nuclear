package com.shootforever.nuclear.module.modules.player;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.MotionUpdateEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.NotifyUtil;
import com.shootforever.nuclear.value.values.BooleanValue;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Eagle extends Module {
    private final BooleanValue autoBuild = new BooleanValue(this, "AutoBuild", true);

    public Eagle() {
        super("Eagle", Category.Player);
    }

    private int test = 0;

    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (mc.player == null || mc.level == null || mc.player.isOnGround()) return;

        test ++;
        NotifyUtil.notifyAsMessage(String.valueOf(test));

        if (mc.level.getBlockState(new BlockPos(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ())).getBlock() instanceof AirBlock) {
            KeyMapping.set(mc.options.keyShift.getKey(), true);

            if (mc.player.getMainHandItem().getItem() instanceof BlockItem
                    || mc.player.getOffhandItem().getItem() instanceof BlockItem && this.autoBuild.getValue()) {
                BlockHitResult hitResult = (BlockHitResult) mc.hitResult;
                if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                    BlockState state = mc.level.getBlockState(blockPos);
                    if (state.getBlock() instanceof AirBlock) {
                        KeyMapping.set(mc.options.keyUse.getKey(), true);
                    }
                }
            }
        } else {
            KeyMapping.set(mc.options.keyShift.getKey(), false);
        }
    }
}
