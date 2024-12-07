package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;
import org.jetbrains.annotations.NotNull;

public class ServerConnectEvent extends Event {
    private final @NotNull String address;

    public ServerConnectEvent(@NotNull String address) {
        this.address = address;
    }

    public @NotNull String getAddress() {
        return address;
    }
}
