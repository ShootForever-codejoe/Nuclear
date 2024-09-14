package com.shootforever.nuclear.value;

public abstract class SingleValue<T> extends Value {
    private T value;

    protected SingleValue(String name, T value) {
        super(name);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
