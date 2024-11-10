package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;

public class WorldChangeEvent extends Event {
    private final String ip;

    public WorldChangeEvent(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }
}
