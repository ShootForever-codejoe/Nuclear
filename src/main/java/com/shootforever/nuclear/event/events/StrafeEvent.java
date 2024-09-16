package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;

public class StrafeEvent extends Event {
    private float yaw;

    public StrafeEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
