package com.shootforever.nuclear.value;

import com.shootforever.nuclear.module.Module;
import org.jetbrains.annotations.NotNull;

public abstract class Value<T> {
    protected final Module module;
    protected final String name;
    protected T value;
    protected final Hide hide;

    protected Value(@NotNull Module module, @NotNull String name, @NotNull T value, Hide hide) {
        this.module = module;
        module.registerValue(this);
        this.name = name;
        this.value = value;
        this.hide = hide == null ? (() -> false) : hide;
    }

    public Module getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(@NotNull T value) {
        this.value = value;
    }

    public Hide getHide() {
        return hide;
    }
}
