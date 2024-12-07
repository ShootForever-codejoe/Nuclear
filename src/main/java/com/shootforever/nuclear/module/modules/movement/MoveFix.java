package com.shootforever.nuclear.module.modules.movement;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.JumpEvent;
import com.shootforever.nuclear.event.events.MotionUpdateEvent;
import com.shootforever.nuclear.event.events.MovementInputEvent;
import com.shootforever.nuclear.event.events.StrafeEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import net.minecraft.util.Mth;

public class MoveFix extends Module {
    private Float yaw, pitch;
    private boolean needUpdate = false;

    public MoveFix() {
        super("MoveFix", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (needUpdate) {
            event.setYaw(yaw);
            event.setPitch(pitch);
            needUpdate = false;
        }
    }

    @EventTarget
    public void onStrafe(StrafeEvent event) {
        if (needUpdate) {
            event.setYaw(yaw);
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (needUpdate) {
            event.setYaw(yaw);
        }
    }

    @EventTarget
    public void onMovementInput(MovementInputEvent event) {
        if (mc.player == null || yaw == null || pitch == null || !needUpdate) return;

        final float forward = event.getForward();
        final float strafe = event.getStrafe();

        final double angle = Mth.wrapDegrees(Math.toDegrees(direction(mc.player.getYRot(), forward, strafe)));

        if (forward == 0 && strafe == 0) return;

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1f; predictedForward <= 1f; predictedForward += 1f) {
            for (float predictedStrafe = -1f; predictedStrafe <= 1f; predictedStrafe += 1f) {
                if (predictedForward == 0 && predictedStrafe == 0) continue;

                final double predictedAngle = Mth.wrapDegrees(Math.toDegrees(direction(yaw, predictedForward, predictedStrafe)));
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }

        event.setForward(closestForward);
        event.setStrafe(closestStrafe);
    }

    public double direction(float yaw, final double forward, final double strafe) {
        if (forward < 0f) yaw += 180f;

        float forward2 = 1f;

        if (forward < 0f) forward2 -= 0.5f;
        else if (forward > 0f) forward2 = 0.5f;

        if (strafe > 0f) yaw -= 90f * forward2;
        else if (strafe < 0f) yaw += 90f * forward2;

        return Math.toRadians(yaw);
    }

    public void setAngle(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        needUpdate = true;
    }
}
