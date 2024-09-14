package com.shootforever.nuclear.value.values;

import com.shootforever.nuclear.value.Value;

import java.util.List;

public class ChoiceValue extends Value<String> {
    private final List<String> choices;

    public ChoiceValue(String name, String value, List<String> choices) {
        super(name, value);
        this.choices = List.copyOf(choices);
    }

    public List<String> getChoices() {
        return choices;
    }
}
