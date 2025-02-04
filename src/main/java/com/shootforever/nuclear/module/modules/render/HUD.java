package com.shootforever.nuclear.module.modules.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.Render2DEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.ColorUtil;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.value.values.NumberValue;

import java.util.List;

public class HUD extends Module {
    private final ChoiceValue mode = new ChoiceValue(this, "Mode", "RGB", List.of(
            "RGB",
            "Rainbow"
    ));
    private final NumberValue red = new NumberValue(this, "Red", 255f, 0f, 255f, 1f,
            () -> !mode.getValue().equalsIgnoreCase("RGB"));
    private final NumberValue green = new NumberValue(this, "Green", 255f, 0f, 255f, 1f,
            () -> !mode.getValue().equalsIgnoreCase("RGB"));
    private final NumberValue blue = new NumberValue(this, "Blue", 255f, 0f, 255f, 1f,
            () -> !mode.getValue().equalsIgnoreCase("RGB"));
    private final NumberValue rainbowSpeed = new NumberValue(this, "RainbowSpeed", 10f, 1f, 100f, 1f,
            () -> !mode.getValue().equalsIgnoreCase("Rainbow"));
    private final NumberValue y = new NumberValue(this, "Y", 24f, 0f, 100f, 0.1f);
    private final NumberValue rowledge = new NumberValue(this, "Rowledge", 12f, 0f, 30f, 0.1f);

    public HUD() {
        super("HUD", Category.RENDER);
    }

    @EventTarget
    public void onRender(Render2DEvent event) {
        mc.font.drawShadow(new PoseStack(), Nuclear.CLIENT_NAME + " " + Nuclear.CLIENT_VERSION, 4, y.getValue(), ColorUtil.color(255, 255, 0, 0));
        int moduleN = 0;
        for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
            if (module.isEnabled()) {
                moduleN++;
                mc.font.drawShadow(new PoseStack(), module.getName(), 4, y.getValue() + moduleN * rowledge.getValue(),
                        switch (mode.getValue()) {
                            case "RGB" -> ColorUtil.color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue(), 0);
                            case "Rainbow" -> ColorUtil.rainbow(rainbowSpeed.getValue().intValue(), 1).getRGB();
                            default -> -1;
                        });
            }
        }
    }
}
