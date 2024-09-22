package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.CancellableEvent;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;

public class PacketEvent extends CancellableEvent {
    private final @NotNull Side side;
    private final @NotNull Packet<?> packet;

    public PacketEvent(@NotNull Side side, @NotNull Packet<?> packet) {
        this.side = side;
        this.packet = packet;
    }

    public @NotNull Side getSide() {
        return side;
    }

    public @NotNull Packet<?> getPacket() {
        return packet;
    }
}

