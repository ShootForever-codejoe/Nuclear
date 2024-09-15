package com.shootforever.nuclear.value.values;

import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ChoiceValue extends Value<String> {
    private final List<String> choices;

    public ChoiceValue(Module module, String name, String choice, List<String> choices) {
        super(module, name, choice);
        this.choices = new ArrayList<>(choices);
    }

    public List<String> getChoices() {
        return new ArrayList<>(choices);
    }

    public boolean isChoice(String name) {
        for (String choice : choices) {
            if (choice.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
