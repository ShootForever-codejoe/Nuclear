package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;

public class KeyPressEvent implements Event {
    private final int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
