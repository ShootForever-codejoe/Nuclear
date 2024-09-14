package com.shootforever.nuclear.value.singlevalues;

import com.shootforever.nuclear.value.SingleValue;

public class IntegerSingleValue extends SingleValue<Integer> {
    private final int min;
    private final int max;
    private final String suffix;

    public IntegerSingleValue(String name, int value, int min, int max, String suffix) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public IntegerSingleValue(String name, int value, int min, int max) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.suffix = null;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getSuffix() {
        return suffix;
    }
}
