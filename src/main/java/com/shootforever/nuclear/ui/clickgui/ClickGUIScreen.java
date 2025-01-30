package com.shootforever.nuclear.ui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.util.ConfigUtil;
import com.shootforever.nuclear.util.NotifyUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import com.shootforever.nuclear.module.Category;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGUIScreen extends Screen {
    public static final ClickGUIScreen INSTANCE = new ClickGUIScreen();
    private final List<Frame> frames = new ArrayList<>();

    protected ClickGUIScreen() {
        super(Component.nullToEmpty("Click GUI"));
        int offset = 20;

        for (Category category : Category.values()) {
            this.frames.add(new Frame(offset, 20, 120, 25, category));
            offset += 140;
        }
    }

    public static void drawRoundedRect(PoseStack poseStack, int x, int y, int width, int height, int cornerRadius, int color) {
        fill(poseStack, x + cornerRadius, y, x + width - cornerRadius, y + height, color);
        fill(poseStack, x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color);
        fill(poseStack, x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color);
        fillCircle(poseStack, x + cornerRadius, y + cornerRadius, cornerRadius, color);
        fillCircle(poseStack, x + width - cornerRadius, y + cornerRadius, cornerRadius, color);
        fillCircle(poseStack, x + cornerRadius, y + height - cornerRadius, cornerRadius, color);
        fillCircle(poseStack, x + width - cornerRadius, y + height - cornerRadius, cornerRadius, color);
    }

    private static void fillCircle(PoseStack poseStack, int centerX, int centerY, int radius, int color) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x * x + y * y <= radius * radius) {
                    poseStack.pushPose();
                    poseStack.translate(centerX + x, centerY + y, 0.0);
                    fill(poseStack, 0, 0, 1, 1, color);
                    poseStack.popPose();
                }
            }
        }
    }

    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);

        for (Frame frame : this.frames) {
            frame.render(poseStack, mouseX, mouseY, partialTicks);
            frame.updatePosition(mouseX, mouseY);
        }

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    public void onClose() {
        super.onClose();

        for (Frame frame : this.frames) {
            frame.onClose();
        }

        try {
            ConfigUtil.saveConfig();
        } catch (IOException e) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "自动保存配置失败");
        }
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Frame frame : this.frames) {
            frame.mouseReleased(mouseX, mouseY, button);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Frame frame : this.frames) {
            frame.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void init() {
        super.init();
    }
}
