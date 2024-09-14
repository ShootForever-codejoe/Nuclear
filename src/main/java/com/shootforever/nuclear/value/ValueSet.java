package com.shootforever.nuclear.value;

import java.util.List;

public class ValueSet extends Value {
    private final List<Value> values;
    private boolean enabled;

    public ValueSet(String name, boolean enabled, List<Value> values) {
        super(name);
        this.enabled = enabled;
        this.values = List.copyOf(values);
    }

    public List<Value> getValues() {
        return values;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
