package com.shootforever.nuclear.value.values;

import com.shootforever.nuclear.value.Value;

public class FloatValue extends Value<Float> {
    private final float min;
    private final float max;
    private final String suffix;

    public FloatValue(String name, float value, float min, float max, String suffix) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public FloatValue(String name, float value, float min, float max) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.suffix = null;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public String getSuffix() {
        return suffix;
    }
}
