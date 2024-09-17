package com.shootforever.nuclear.event.events;

import com.sun.jdi.event.Event;

public abstract class CancellableEvent implements Event, Cancellable {
    private boolean cancelled;


    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
