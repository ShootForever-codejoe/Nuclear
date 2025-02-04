package com.shootforever.nuclear.command;

import org.jetbrains.annotations.NotNull;

public abstract class Command {
    protected final String name;

    protected Command(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(@NotNull String @NotNull [] args);
}
