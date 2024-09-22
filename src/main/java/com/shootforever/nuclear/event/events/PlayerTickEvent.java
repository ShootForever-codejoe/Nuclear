package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;
import org.jetbrains.annotations.NotNull;

public class PlayerTickEvent extends Event {
    private final @NotNull Side side;

    public PlayerTickEvent(@NotNull Side side) {
        this.side = side;
    }

    public @NotNull Side getSide() {
        return side;
    }
}
