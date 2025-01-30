package com.shootforever.nuclear.util;

import com.shootforever.nuclear.Nuclear;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

public final class BlockUtil {
    private static final Minecraft mc = Nuclear.mc;

    private BlockUtil() {
        throw new AssertionError();
    }

    public static boolean isAirBlock(@NotNull BlockPos blockPos) {
        return mc.level != null && mc.level.isEmptyBlock(blockPos);
    }

    public static boolean isValidBlock(@NotNull BlockPos blockPos) {
        return mc.level != null && isValidBlock(mc.level.getBlockState(blockPos).getBlock());
    }

    public static boolean isValidBlock(@NotNull Block block) {
        return !(block instanceof LiquidBlock)
                && !(block instanceof AirBlock)
                && !(block instanceof ChestBlock)
                && !(block instanceof FurnaceBlock)
                && !(block instanceof LadderBlock)
                && !(block instanceof TntBlock);
    }
}
