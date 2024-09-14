package com.shootforever.nuclear.module.modules.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.Render2DEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;

public class HUD extends Module {
    private int y = 24;
    private int interval = 10;

    public HUD() {
        super("HUD", Category.Render);
        setEnabled(true);
    }

    @EventTarget
    public void onRender(Render2DEvent event) {
        mc.font.drawShadow(new PoseStack(), "Nuclear " + Nuclear.version, 4, y, -1);
        int module_n = 0;
        for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
            if (module.isEnabled()) {
                module_n ++;
                mc.font.drawShadow(new PoseStack(), module.getName(), 4, y + module_n * interval, -1);
            }
        }
    }
}
