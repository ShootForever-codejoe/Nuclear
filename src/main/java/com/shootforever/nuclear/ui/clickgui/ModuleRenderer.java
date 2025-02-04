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
    private final Module module;
    private final Frame parent;
    private int offset;
    private final List<Component> components;
    private boolean extended;
    private float openProgress;
    private long lastToggleTime;

    public ModuleRenderer(@NotNull Module module, @NotNull Frame parent, int offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;
        extended = false;
        openProgress = 0.0F;
        lastToggleTime = 0L;
        components = new ArrayList<>();
        int valueOffset = 20;

        for (Value<?> value : module.getValues()) {
            if (value instanceof BooleanValue booleanValue) {
                components.add(new BooleanValueComponent(booleanValue, this, valueOffset));
            } else if (value instanceof ChoiceValue choiceValue) {
                components.add(new ChoiceValueComponent(choiceValue, this, valueOffset));
            } else if (value instanceof NumberValue numberValue) {
                components.add(new NumberValueComponent(numberValue, this, valueOffset));
            }

            valueOffset += 20;
        }
    }

    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        updateAnimation();
        ClickGUIScreen.drawRoundedRect(
                stack, x, y, width, height, 0, ColorUtil.color(0, 0, 0, isHovered(mouseX, mouseY, x, y, width, height) ? 200 : 160)
        );
        int textOffset = 10 - 9 / 2;
        String moduleName = module.getName();
        float scaleFactor = 1.0F;
        int moduleNameWidth = mc.font.width(moduleName);
        if (moduleNameWidth > width - 30) {
            scaleFactor = (float) (width - 30) / (float) moduleNameWidth;
        }

        stack.pushPose();
        stack.translate(x + textOffset, y + textOffset, 0.0);
        stack.scale(scaleFactor, scaleFactor, 1.0F);
        mc.font.drawShadow(stack, moduleName, 0.0F, 0.0F, module.isEnabled() ? Color.GREEN.getRGB() : -1);
        stack.popPose();
        if (!components.isEmpty()) {
            mc.font.drawShadow(stack, extended ? "-" : "+", (float) (x + width - 14), (float) (y + textOffset), -1);
        }

        if (openProgress > 0.0F) {
            int componentY = y + 20;

            for (Component component : components) {
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
        float targetProgress = extended ? 1.0F : 0.0F;
        if (openProgress != targetProgress) {
            float deltaTime = (float) (currentTime - lastToggleTime) / 1000.0F;
            openProgress = extended ? Math.min(1.0F, openProgress + deltaTime) : Math.max(0.0F, openProgress - deltaTime);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY, parent.getX(), parent.getY() + offset, parent.getWidth(), 20)) {
            if (mouseButton == 0) {
                module.setEnabled(!module.isEnabled());
            } else if (mouseButton == 1 && !components.isEmpty()) {
                extended = !extended;
                lastToggleTime = System.currentTimeMillis();
                parent.updateButtons();
            }
        }

        if (extended) {
            for (Component component : components) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (extended) {
            for (Component component : components) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX > (double) x && mouseX < (double) (x + width) && mouseY > (double) y && mouseY < (double) (y + height);
    }

    public int getHeight() {
        return 20 + (extended ? components.size() * 20 : 0);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Component> getComponents() {
        return new ArrayList<>(components);
    }

    public boolean isExtended() {
        return extended;
    }

    public Frame getParent() {
        return parent;
    }

    public int getOffset() {
        return offset;
    }
}
