package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;

public class MotionEvent extends Event {
    private Side side;


    public MotionEvent(Side side) {
        this.side = side;
    }

    public Event.Side getSide() {
        return this.side;
    }

    public void setSide(Side side) {
        this.side = side;
    }
}
