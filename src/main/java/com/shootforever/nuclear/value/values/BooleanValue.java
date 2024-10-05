package com.shootforever.nuclear.value.values;

import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.Hide;
import com.shootforever.nuclear.value.Value;
import org.jetbrains.annotations.NotNull;

public class BooleanValue extends Value<Boolean> {
    public BooleanValue(@NotNull Module module, @NotNull String name, boolean value, @NotNull Hide hide) {
        super(module, name, value, hide);
    }

    public BooleanValue(@NotNull Module module, @NotNull String name, boolean value) {
        super(module, name, value);
    }
}
