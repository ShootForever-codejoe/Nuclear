package com.shootforever.nuclear.ui.clickgui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.value.Value;
import com.shootforever.nuclear.util.functions.ColorUtil;
import net.minecraft.client.Minecraft;

public class Component {
    private final Minecraft mc = Nuclear.mc;
    public Value<?> value;
    public ModuleRenderer parent;
    public int offset;

    public Component(Value<?> value, ModuleRenderer parent, int offset) {
        this.value = value;
        this.parent = parent;
        this.offset = offset;
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        ClickGUIScreen.drawRoundedRect(
                stack, x, y, width, height, 3, ColorUtil.color(0, 0, 0, this.isHovered(mouseX, mouseY, x, y, width, height) ? 180 : 140)
        );
        int textOffset = height / 2 - 9 / 2;
        mc.font.drawShadow(stack, this.value.getName(), (float) (x + 5), (float) (y + textOffset), -1);
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
    }

    public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX > (double) x && mouseX < (double) (x + width) && mouseY > (double) y && mouseY < (double) (y + height);
    }
}
