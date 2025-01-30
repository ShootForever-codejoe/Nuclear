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
    public int x;
    public int y;
    public int dragX;
    public int dragY;
    public int width;
    public int height;
    public Category category;
    public boolean dragging;
    public boolean extended;
    private final List<ModuleRenderer> renderers;
    private float openProgress;
    private long lastToggleTime;

    public Frame(int x, int y, int width, int height, @NotNull Category category) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category = category;
        this.dragging = false;
        this.extended = false;
        this.openProgress = 0.0F;
        this.lastToggleTime = 0L;
        this.renderers = new ArrayList<>();
        int offset = height;

        for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
            if (module.getCategory() == category) {
                this.renderers.add(new ModuleRenderer(module, this, offset));
                offset += height;
            }
        }
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        this.updateAnimation();
        ClickGUIScreen.drawRoundedRect(stack, this.x, this.y, this.width, this.height, 0, ColorUtil.color(0, 0, 0, 150));
        mc.font.drawShadow(stack, this.category.name(), (float) (this.x + 25), (float) this.y + ((float) this.height / 2.0F - 9.0F / 2.0F), -1);
        mc.font.drawShadow(stack, this.extended ? "-" : "+", (float) (this.x + this.width - 14), (float) this.y + ((float) this.height / 2.0F - 9.0F / 2.0F), -1);
        long currentTime = System.currentTimeMillis();
        float targetProgress = this.extended ? 1.0F : 0.0F;
        if (this.openProgress != targetProgress) {
            float deltaTime = (float) (currentTime - this.lastToggleTime) / 1000.0F;
            this.openProgress = this.extended ? Math.min(1.0F, this.openProgress + deltaTime) : Math.max(0.0F, this.openProgress - deltaTime);
        }

        if (this.openProgress > 0.0F) {
            int yOffset = this.height;

            for (ModuleRenderer renderer : this.renderers) {
                int moduleHeight = (int) ((float) renderer.getHeight() * this.openProgress);
                renderer.render(stack, mouseX, mouseY, delta, this.x, this.y + yOffset, this.width, moduleHeight);
                yOffset += renderer.getHeight();
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.dragging = true;
                this.dragX = (int) (mouseX - (double) this.x);
                this.dragY = (int) (mouseY - (double) this.y);
            } else if (mouseButton == 1) {
                this.extended = !this.extended;
                this.lastToggleTime = System.currentTimeMillis();
            }
        }

        if (this.extended) {
            for (ModuleRenderer renderer : this.renderers) {
                renderer.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (ModuleRenderer renderer : this.renderers) {
            renderer.mouseReleased(mouseX, mouseY, mouseButton);
        }

        if (mouseButton == 0 && this.dragging) {
            this.dragging = false;
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > (double) this.x && mouseX < (double) (this.x + this.width) && mouseY > (double) this.y && mouseY < (double) (this.y + this.height);
    }

    public void updatePosition(double mouseX, double mouseY) {
        if (this.dragging) {
            this.x = (int) (mouseX - (double) this.dragX);
            this.y = (int) (mouseY - (double) this.dragY);
        }
    }

    public void updateButtons() {
        int offset = this.height;

        for (ModuleRenderer renderer : this.renderers) {
            renderer.offset = offset;
            offset += this.height;
            if (renderer.extended) {
                for (Component component : renderer.components) {
                    if (component.value.getHide().hide()) {
                        offset += this.height;
                    }
                }
            }
        }
    }

    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        float targetProgress = this.extended ? 1.0F : 0.0F;
        if (this.openProgress != targetProgress) {
            float deltaTime = (float) (currentTime - this.lastToggleTime) / 1000.0F;
            this.openProgress = this.extended ? Math.min(1.0F, this.openProgress + deltaTime) : Math.max(0.0F, this.openProgress - deltaTime);
        }

        this.updateButtons();
    }

    public void onClose() {
        this.extended = false;
        this.openProgress = 0.0F;
    }
}
