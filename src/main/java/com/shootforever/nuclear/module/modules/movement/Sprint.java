package com.shootforever.nuclear.module.modules.movement;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.MotionUpdateEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import net.minecraft.client.KeyMapping;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.Movement);
        setEnabled(true);
    }

    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (mc.player == null) return;

        if (mc.player.horizontalCollision || mc.player.zza <= 0) return;

        mc.options.toggleSprint = true;
        KeyMapping.set(mc.options.keySprint.getKey(), true);
    }
}
