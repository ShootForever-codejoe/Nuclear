package com.shootforever.nuclear.value;

public abstract class Value<T> extends BaseValue {
    private T value;

    public Value(String name, T value) {
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
