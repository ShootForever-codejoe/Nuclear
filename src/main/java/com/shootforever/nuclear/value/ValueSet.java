package com.shootforever.nuclear.value;

import java.util.List;

public class ValueSet extends BaseValue {
    private final List<BaseValue> values;
    private boolean enabled;

    public ValueSet(String name, boolean enabled, List<BaseValue> values) {
        super(name);
        this.enabled = enabled;
        this.values = List.copyOf(values);
        for (BaseValue value : values) {
            value.setValueSet(this);
        }
    }

    public List<BaseValue> getValues() {
        return values;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
