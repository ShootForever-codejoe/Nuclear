package com.shootforever.nuclear.ui.clickgui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.util.ColorUtil;
import net.minecraft.client.Minecraft;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private final Minecraft mc = Nuclear.mc;
    private int x;
    private int y;
    private int dragX;
    private int dragY;
    private final int width;
    private final int height;
    private final Category category;
    private boolean dragging;
    private boolean extended;
    private final List<ModuleRenderer> renderers;
    private float openProgress;
    private long lastToggleTime;

    public Frame(int x, int y, int width, int height, @NotNull Category category) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category = category;
        dragging = false;
        extended = false;
        openProgress = 0.0F;
        lastToggleTime = 0L;
        renderers = new ArrayList<>();
        int offset = height;

        for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
            if (module.getCategory() == category) {
                renderers.add(new ModuleRenderer(module, this, offset));
                offset += height;
            }
        }
    }

    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float delta) {
        updateAnimation();
        ClickGUIScreen.drawRoundedRect(stack, x, y, width, height, 0, ColorUtil.color(0, 0, 0, 150));
        mc.font.drawShadow(stack, category.name(), (float) (x + 25), (float) y + ((float) height / 2.0F - 9.0F / 2.0F), -1);
        mc.font.drawShadow(stack, extended ? "-" : "+", (float) (x + width - 14), (float) y + ((float) height / 2.0F - 9.0F / 2.0F), -1);
        long currentTime = System.currentTimeMillis();
        float targetProgress = extended ? 1.0F : 0.0F;
        if (openProgress != targetProgress) {
            float deltaTime = (float) (currentTime - lastToggleTime) / 1000.0F;
            openProgress = extended ? Math.min(1.0F, openProgress + deltaTime) : Math.max(0.0F, openProgress - deltaTime);
        }

        if (openProgress > 0.0F) {
            int yOffset = height;

            for (ModuleRenderer renderer : renderers) {
                int moduleHeight = (int) ((float) renderer.getHeight() * openProgress);
                renderer.render(stack, mouseX, mouseY, delta, x, y + yOffset, width, moduleHeight);
                yOffset += renderer.getHeight();
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                dragging = true;
                dragX = (int) (mouseX - (double) x);
                dragY = (int) (mouseY - (double) y);
            } else if (mouseButton == 1) {
                extended = !extended;
                lastToggleTime = System.currentTimeMillis();
            }
        }

        if (extended) {
            for (ModuleRenderer renderer : renderers) {
                renderer.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (ModuleRenderer renderer : renderers) {
            renderer.mouseReleased(mouseX, mouseY, mouseButton);
        }

        if (mouseButton == 0 && dragging) {
            dragging = false;
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > (double) x && mouseX < (double) (x + width) && mouseY > (double) y && mouseY < (double) (y + height);
    }

    public void updatePosition(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) (mouseX - (double) dragX);
            y = (int) (mouseY - (double) dragY);
        }
    }

    public void updateButtons() {
        int offset = height;

        for (ModuleRenderer renderer : renderers) {
            renderer.setOffset(offset);
            offset += height;
            if (renderer.isExtended()) {
                for (Component component : renderer.getComponents()) {
                    if (component.value.getHide().hide()) {
                        offset += height;
                    }
                }
            }
        }
    }

    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        float targetProgress = extended ? 1.0F : 0.0F;
        if (openProgress != targetProgress) {
            float deltaTime = (float) (currentTime - lastToggleTime) / 1000.0F;
            openProgress = extended ? Math.min(1.0F, openProgress + deltaTime) : Math.max(0.0F, openProgress - deltaTime);
        }

        updateButtons();
    }

    public void onClose() {
        extended = false;
        openProgress = 0.0F;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Category getCategory() {
        return category;
    }
}
