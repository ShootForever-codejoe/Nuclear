package com.shootforever.nuclear.value.values;

import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.Hide;
import com.shootforever.nuclear.value.Value;
import org.jetbrains.annotations.NotNull;

public class NumberValue extends Value<Float> {
    private final float min;
    private final float max;
    private final float step;
    private final String suffix;

    public NumberValue(@NotNull Module module, @NotNull String name, float value, float min, float max, float step, String suffix, Hide hide) {
        super(module, name, value, hide);
        this.min = min;
        this.max = max;
        this.step = step;
        this.suffix = suffix;
    }

    public NumberValue(@NotNull Module module, @NotNull String name, float value, float min, float max, float step, @NotNull Hide hide) {
        this(module, name, value, min, max, step, null, hide);
    }

    public NumberValue(@NotNull Module module, @NotNull String name, float value, float min, float max, float step, @NotNull String suffix) {
        this(module, name, value, min, max, step, suffix, null);
    }

    public NumberValue(@NotNull Module module, @NotNull String name, float value, float min, float max, float step) {
        this(module, name, value, min, max, step, null, null);
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

    public float getStep() {
        return step;
    }
}
