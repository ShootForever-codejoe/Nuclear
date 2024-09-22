package com.shootforever.nuclear.value;

import com.shootforever.nuclear.module.Module;
import org.jetbrains.annotations.NotNull;

public abstract class Value<T> {
    protected final @NotNull Module module;
    protected final @NotNull String name;
    protected @NotNull T value;

    protected Value(@NotNull Module module, @NotNull String name, @NotNull T value) {
        this.module = module;
        module.registerValue(this);
        this.name = name;
        this.value = value;
    }

    public @NotNull Module getModule() {
        return module;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull T getValue() {
        return value;
    }

    public void setValue(@NotNull T value) {
        this.value = value;
    }
}
