package com.shootforever.nuclear.module.modules.render;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.GameTickEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;

public class FullBright extends Module {
    private double old;

    public FullBright() {
        super("FullBright", Category.RENDER);
    }

    @Override
    public void onEnable() {
        old = mc.options.gamma;
    }

    @Override
    public void onDisable() {
        mc.options.gamma = old;
    }

    @EventTarget
    public void onTick(GameTickEvent event) {
        if (mc.options.gamma != 10000d) {
            mc.options.gamma = 10000d;
        }
    }
}
