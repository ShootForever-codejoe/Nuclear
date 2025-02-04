package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.CancellableEvent;
import org.jetbrains.annotations.NotNull;

public class ChatEvent extends CancellableEvent {
    private final String message;

    public ChatEvent(@NotNull String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
