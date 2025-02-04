package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.CancellableEvent;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;

public class PacketEvent extends CancellableEvent {
    private final Side side;
    private final Packet<?> packet;

    public PacketEvent(@NotNull Side side, @NotNull Packet<?> packet) {
        this.side = side;
        this.packet = packet;
    }

    public Side getSide() {
        return side;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}

