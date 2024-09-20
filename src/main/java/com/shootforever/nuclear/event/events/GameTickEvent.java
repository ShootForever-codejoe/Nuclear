package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;

public class GameTickEvent extends Event {
    private final Side side;

    public GameTickEvent(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
