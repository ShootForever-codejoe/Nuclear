package com.shootforever.nuclear.value;

public abstract class BaseValue {
    private final String name;
    private ValueSet valueSet = null;

    public BaseValue(String name) {
        this.name = name;
    }

    public ValueSet getValueSet() {
        return valueSet;
    }

    public void setValueSet(ValueSet valueSet) {
        this.valueSet = valueSet;
    }

    public String getName() {
        return name;
    }
}
