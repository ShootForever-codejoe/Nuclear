package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.CancellableEvent;
import net.minecraft.network.protocol.Packet;

public class PacketEvent extends CancellableEvent {
    private final Side side;
    private final Packet<?> packet;

    public PacketEvent(Side side, Packet<?> packet) {
        this.side = side;
        this.packet = packet;
    }

    public Side getSide() {
        return this.side;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }
}

