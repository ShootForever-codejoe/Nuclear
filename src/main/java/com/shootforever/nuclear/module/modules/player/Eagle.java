package com.shootforever.nuclear.module.modules.player;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.MovementInputEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.functions.EntityUtil;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.NumberValue;
import net.minecraft.client.KeyMapping;

public class Eagle extends Module {
    private final NumberValue distanceToEdge = new NumberValue(this, "DistanceToEdge", 0.1f, 0f, 0.8f, 0.01f);
    private final BooleanValue onlyBackward = new BooleanValue(this, "OnlyBackward", true);

    public Eagle() {
        super("Eagle", Category.PLAYER);
    }

    @EventTarget
    public void onMovementInput(MovementInputEvent event) {
        if (mc.player != null && (!onlyBackward.getValue() || mc.options.keyDown.isDown())) {
            KeyMapping.set(mc.options.keyShift.getKey(), EntityUtil.isCloseToEdge(mc.player, distanceToEdge.getValue()));
        }
    }
}
