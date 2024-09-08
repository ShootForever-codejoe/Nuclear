package com.shootforever.nuclear.event.events;

import com.shootforever.nuclear.event.CancellableEvent;
import net.minecraft.world.entity.Entity;

public class AttackEvent extends CancellableEvent {
    private final Entity entity;

    public AttackEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
