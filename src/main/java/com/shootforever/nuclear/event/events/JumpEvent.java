package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;

public class JumpEvent extends Event {
    private float yaw;

    public JumpEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
