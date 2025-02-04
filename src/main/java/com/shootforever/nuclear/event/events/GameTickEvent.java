package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;
import org.jetbrains.annotations.NotNull;

public class GameTickEvent extends Event {
    private final Side side;

    public GameTickEvent(@NotNull Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
