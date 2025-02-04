package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;
import org.jetbrains.annotations.NotNull;

public class ServerConnectEvent extends Event {
    private final String address;

    public ServerConnectEvent(@NotNull String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
