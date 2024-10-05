package com.shootforever.nuclear.value;

import com.shootforever.nuclear.module.Module;
import org.jetbrains.annotations.NotNull;

public abstract class Value<T> {
    protected final @NotNull Module module;
    protected final @NotNull String name;
    protected @NotNull T value;
    protected final @NotNull Hide hide;

    protected Value(@NotNull Module module, @NotNull String name, @NotNull T value, @NotNull Hide hide) {
        this.module = module;
        module.registerValue(this);
        this.name = name;
        this.value = value;
        this.hide = hide;
    }

    protected Value(@NotNull Module module, @NotNull String name, @NotNull T value) {
        this.module = module;
        module.registerValue(this);
        this.name = name;
        this.value = value;
        this.hide = () -> false;
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

    public @NotNull Hide getHide() {
        return hide;
    }
}
