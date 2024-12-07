package com.shootforever.nuclear.event.listener;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.Listener;
import com.shootforever.nuclear.event.events.JumpEvent;
import com.shootforever.nuclear.event.events.MotionUpdateEvent;
import com.shootforever.nuclear.event.events.StrafeEvent;

public class RotationListener extends Listener {
    private Float yaw, pitch;
    private boolean needUpdate = false;

    public RotationListener() {
        super("RotationListener");
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

    public void setAngle(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        needUpdate = true;
    }
}
