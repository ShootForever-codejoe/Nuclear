package com.shootforever.nuclear.util;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.listener.listeners.RotationListener;
import com.shootforever.nuclear.module.modules.movement.MoveFix;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class RotationUtil {
    private RotationUtil() {
        throw new AssertionError();
    }

    public static double getDistanceToEntityBox(Entity target) {
        if (Nuclear.mc.player == null) return 0;

        Vec3 eyes = Nuclear.mc.player.getEyePosition(1.0F);
        Vec3 pos = getNearestPointBB(eyes, target.getBoundingBox());
        double xDist = Math.abs(pos.x - eyes.x);
        double yDist = Math.abs(pos.y - eyes.y);
        double zDist = Math.abs(pos.z - eyes.z);
        return Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
    }

    @Contract("_, _ -> new")
    private static @NotNull Vec3 getNearestPointBB(@NotNull Vec3 point, @NotNull AABB box) {
        double x = Math.max(box.minX, Math.min(point.x, box.maxX));
        double y = Math.max(box.minY, Math.min(point.y, box.maxY));
        double z = Math.max(box.minZ, Math.min(point.z, box.maxZ));
        return new Vec3(x, y, z);
    }

    public static float @NotNull [] getRotationFromEyeToPoint(Vec3 point3d) {
        if (Nuclear.mc.player != null) {
            return getRotation(new Vec3(Nuclear.mc.player.getX(), Nuclear.mc.player.getBoundingBox().minY + (double) Nuclear.mc.player.getEyeHeight(), Nuclear.mc.player.getZ()), point3d);
        } else {
            return new float[]{0, 0};
        }
    }

    @Contract("_, _ -> new")
    public static float @NotNull [] getRotation(@NotNull Vec3 from, @NotNull Vec3 to) {
        double x = to.x - from.x;
        double y = to.y - from.y;
        double z = to.z - from.z;
        double sqrt = Math.sqrt(x * x + z * z);
        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, sqrt));
        return new float[]{yaw, Math.min(Math.max(pitch, -90f), 90f)};
    }

    public static float @NotNull [] getSimpleRotations(LivingEntity target) {
        if (Nuclear.mc.player == null) return new float[]{0, 0};

        double yDist = target.getY() - Nuclear.mc.player.getY();
        Vec3 targetPos;
        if (yDist >= 1.547) {
            targetPos = new Vec3(target.getX(), target.getY(), target.getZ());
        } else if (yDist <= -1.547) {
            targetPos = new Vec3(target.getX(), target.getY() + target.getEyeHeight(), target.getZ());
        } else {
            targetPos = new Vec3(target.getX(), target.getY() + target.getEyeHeight() / 2, target.getZ());
        }

        return getRotationFromEyeToPoint(targetPos);
    }

    public static void setAngleSilently(float yaw, float pitch) {
        MoveFix moveFix = (MoveFix) Nuclear.getInstance().getModuleManager().getModule("MoveFix");
        RotationListener rotationListener = (RotationListener) Nuclear.getInstance().getListenerManager().getListener("RotationListener");
        if (moveFix != null && moveFix.isEnabled()) {
            moveFix.setAngle(yaw, pitch);
        } else if (rotationListener != null) {
            rotationListener.setAngle(yaw, pitch);
        }
    }
}
