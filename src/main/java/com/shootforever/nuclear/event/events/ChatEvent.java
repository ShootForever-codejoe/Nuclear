package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.CancellableEvent;
import org.jetbrains.annotations.NotNull;

public class ChatEvent extends CancellableEvent {
    private final @NotNull String message;

    public ChatEvent(@NotNull String message) {
        this.message = message;
    }

    public @NotNull String getMessage() {
        return message;
    }
}
