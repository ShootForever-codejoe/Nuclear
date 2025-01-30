package com.shootforever.nuclear.value.values;

import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.Hide;
import com.shootforever.nuclear.value.Value;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChoiceValue extends Value<String> {
    private final List<String> choices;

    public ChoiceValue(@NotNull Module module, @NotNull String name, @NotNull String choice, @NotNull List<@NotNull String> choices, Hide hide) {
        super(module, name, choice, hide);
        this.choices = new ArrayList<>(choices);
    }

    public ChoiceValue(@NotNull Module module, @NotNull String name, @NotNull String choice, @NotNull List<@NotNull String> choices) {
        this(module, name, choice, choices, null);
    }

    public List<String> getChoices() {
        return new ArrayList<>(choices);
    }

    public boolean isChoice(@NotNull String name) {
        for (String choice : choices) {
            if (choice.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
