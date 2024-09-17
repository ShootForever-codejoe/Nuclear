package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.Event;
import net.minecraft.network.protocol.Packet;

public abstract class PacketEvent extends CancellableEvent {
    private final Event.Side side;
    private final Packet<?> packet;


    public PacketEvent(Event.Side side, Packet<?> packet) {
        this.side = side;
        this.packet = packet;
    }


    public Event.Side getSide() {
        return this.side;
    }


    public Packet<?> getPacket() {
        return this.packet;
    }
}

