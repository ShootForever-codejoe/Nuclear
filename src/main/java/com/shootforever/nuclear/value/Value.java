package com.shootforever.nuclear.value;

import com.shootforever.nuclear.module.Module;

public abstract class Value<T> {
    protected final Module module;
    protected final String name;
    protected T value;

    protected Value(Module module, String name, T value) {
        this.module = module;
        module.registerValue(this);
        this.name = name;
        this.value = value;
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

    public void setValue(T value) {
        this.value = value;
    }
}
