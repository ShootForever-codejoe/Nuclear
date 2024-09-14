package com.shootforever.nuclear.value;

import java.util.ArrayList;
import java.util.List;

public abstract class Value {
    private final String name;

    protected Value(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<SingleValue> getSingleValues(List<Value> values) {
        List<SingleValue> singleValues = new ArrayList<>();
        for (Value value : values) {
            if (value instanceof SingleValue) {
                singleValues.add((SingleValue) value);
            } else if (value instanceof ValueSet) {
                singleValues.addAll(getSingleValues(((ValueSet) value).getValues()));
            }
        }
        return singleValues;
    }
}
