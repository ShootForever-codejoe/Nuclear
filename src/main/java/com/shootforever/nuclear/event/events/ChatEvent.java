package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.CancellableEvent;

public class ChatEvent extends CancellableEvent {
    private final String message;

    public ChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
