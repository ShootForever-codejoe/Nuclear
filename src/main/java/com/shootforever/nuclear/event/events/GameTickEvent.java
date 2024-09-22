package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;
import org.jetbrains.annotations.NotNull;

public class GameTickEvent extends Event {
    private final @NotNull Side side;

    public GameTickEvent(@NotNull Side side) {
        this.side = side;
    }

    public @NotNull Side getSide() {
        return side;
    }
}
