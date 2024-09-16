package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;

public class MovementInputEvent extends Event {
    private float forward, strafe;

    public MovementInputEvent(float forward, float strafe) {
        this.forward = forward;
        this.strafe = strafe;
    }

    public float getForward() {
        return forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }
}
