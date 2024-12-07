package com.shootforever.nuclear.event;

import org.jetbrains.annotations.NotNull;

public abstract class Listener {
    protected final @NotNull String name;

    protected Listener(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
