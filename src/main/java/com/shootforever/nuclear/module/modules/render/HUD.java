package com.shootforever.nuclear.module.modules.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.Render2DEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.value.values.NumberValue;
import net.minecraft.ChatFormatting;

import java.util.ArrayList;

public class HUD extends Module {
    private final ChoiceValue color = new ChoiceValue(this, "Color", "White", new ArrayList<>(ChatFormatting.getNames(true, true)));
    private final NumberValue y = new NumberValue(this, "Y", 24f, 0f, 100f, 0.1f);
    private final NumberValue rowledge = new NumberValue(this, "Rowledge", 12f, 0f, 30f, 0.1f);

    public HUD() {
        super("HUD", Category.RENDER);
    }

    @EventTarget
    public void onRender(Render2DEvent event) {
        mc.font.drawShadow(new PoseStack(), ChatFormatting.getByName(color.getValue()) + Nuclear.CLIENT_NAME + " " + Nuclear.CLIENT_VERSION, 4, y.getValue(), -1);
        int moduleN = 0;
        for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
            if (module.isEnabled()) {
                moduleN ++;
                mc.font.drawShadow(new PoseStack(), ChatFormatting.getByName(color.getValue()) + module.getName(), 4, y.getValue() + moduleN * rowledge.getValue(), -1);
            }
        }
    }
}
