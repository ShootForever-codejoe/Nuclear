package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;

public class PlayerTickEvent extends Event {
    private final Side side;

    public PlayerTickEvent(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
