package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.CancellableEvent;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class AttackEvent extends CancellableEvent {
    private final Entity entity;

    public AttackEvent(@NotNull Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
