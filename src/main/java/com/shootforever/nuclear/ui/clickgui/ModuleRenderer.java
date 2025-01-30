package com.shootforever.nuclear.ui.clickgui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.Value;
import com.shootforever.nuclear.value.values.NumberValue;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.ui.clickgui.components.BooleanValueComponent;
import com.shootforever.nuclear.ui.clickgui.components.ChoiceValueComponent;
import com.shootforever.nuclear.ui.clickgui.components.NumberValueComponent;
import com.shootforever.nuclear.util.ColorUtil;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleRenderer {
    private final Minecraft mc = Nuclear.mc;
    public Module module;
    public Frame parent;
    public int offset;
    public List<Component> components;
    public boolean extended;
    private float openProgress;
    private long lastToggleTime;

    public ModuleRenderer(@NotNull Module module, Frame parent, int offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;
        this.extended = false;
        this.openProgress = 0.0F;
        this.lastToggleTime = 0L;
        this.components = new ArrayList<>();
        int valueOffset = 20;

        for (Value<?> value : module.getValues()) {
            if (value instanceof BooleanValue booleanValue) {
                this.components.add(new BooleanValueComponent(booleanValue, this, valueOffset));
            } else if (value instanceof ChoiceValue choiceValue) {
                this.components.add(new ChoiceValueComponent(choiceValue, this, valueOffset));
            } else if (value instanceof NumberValue numberValue) {
                this.components.add(new NumberValueComponent(numberValue, this, valueOffset));
            }

            valueOffset += 20;
        }
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        this.updateAnimation();
        ClickGUIScreen.drawRoundedRect(
                stack, x, y, width, height, 0, ColorUtil.color(0, 0, 0, this.isHovered(mouseX, mouseY, x, y, width, height) ? 200 : 160)
        );
        int textOffset = 10 - 9 / 2;
        String moduleName = this.module.getName();
        float scaleFactor = 1.0F;
        int moduleNameWidth = mc.font.width(moduleName);
        if (moduleNameWidth > width - 30) {
            scaleFactor = (float) (width - 30) / (float) moduleNameWidth;
        }

        stack.pushPose();
        stack.translate(x + textOffset, y + textOffset, 0.0);
        stack.scale(scaleFactor, scaleFactor, 1.0F);
        mc.font.drawShadow(stack, moduleName, 0.0F, 0.0F, this.module.isEnabled() ? Color.GREEN.getRGB() : -1);
        stack.popPose();
        if (!this.components.isEmpty()) {
            mc.font.drawShadow(stack, this.extended ? "-" : "+", (float) (x + width - 14), (float) (y + textOffset), -1);
        }

        if (this.openProgress > 0.0F) {
            int componentY = y + 20;

            for (Component component : this.components) {
                if (componentY + 20 > y + height) {
                    break;
                }

                component.render(stack, mouseX, mouseY, delta, x, componentY, width, 20);
                componentY += 20;
            }
        }
    }

    private void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        float targetProgress = this.extended ? 1.0F : 0.0F;
        if (this.openProgress != targetProgress) {
            float deltaTime = (float) (currentTime - this.lastToggleTime) / 1000.0F;
            this.openProgress = this.extended ? Math.min(1.0F, this.openProgress + deltaTime) : Math.max(0.0F, this.openProgress - deltaTime);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY, this.parent.x, this.parent.y + this.offset, this.parent.width, 20)) {
            if (mouseButton == 0) {
                this.module.setEnabled(!this.module.isEnabled());
            } else if (mouseButton == 1 && !this.components.isEmpty()) {
                this.extended = !this.extended;
                this.lastToggleTime = System.currentTimeMillis();
                this.parent.updateButtons();
            }
        }

        if (this.extended) {
            for (Component component : this.components) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (this.extended) {
            for (Component component : this.components) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX > (double) x && mouseX < (double) (x + width) && mouseY > (double) y && mouseY < (double) (y + height);
    }

    public int getHeight() {
        return 20 + (this.extended ? this.components.size() * 20 : 0);
    }
}
