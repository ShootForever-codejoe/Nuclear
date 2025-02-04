package com.shootforever.nuclear.listener;

import org.jetbrains.annotations.NotNull;

public abstract class Listener {
    protected final @NotNull String name;

    @SuppressWarnings("SameParameterValue")
    protected Listener(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
