package com.shootforever.nuclear.value.singlevalues;

import com.shootforever.nuclear.value.SingleValue;

import java.util.List;

public class ChoiceSingleValue extends SingleValue<String> {
    private final List<String> choices;

    public ChoiceSingleValue(String name, String value, List<String> choices) {
        super(name, value);
        this.choices = List.copyOf(choices);
    }

    public List<String> getChoices() {
        return choices;
    }
}
