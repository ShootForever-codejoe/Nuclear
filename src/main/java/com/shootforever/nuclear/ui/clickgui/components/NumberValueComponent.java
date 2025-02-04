package com.shootforever.nuclear.ui.clickgui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.ui.clickgui.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import com.shootforever.nuclear.value.values.NumberValue;
import com.shootforever.nuclear.ui.clickgui.ModuleRenderer;
import com.shootforever.nuclear.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class NumberValueComponent extends Component {
    private final Minecraft mc = Nuclear.mc;
    private final NumberValue numValue;
    private boolean sliding = false;

    public NumberValueComponent(NumberValue value, ModuleRenderer parent, int offset) {
        super(value, parent, offset);
        numValue = value;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        super.render(stack, mouseX, mouseY, delta, x, y, width, height);
        int renderWidth = (int) ((float) width
                * (numValue.getValue() - numValue.getMin())
                / (numValue.getMax() - numValue.getMin()));
        Gui.fill(stack, x, y + height - 3, x + renderWidth, y + height, Color.WHITE.getRGB());
        if (sliding) {
            double diff = Math.min(width, Math.max(0, mouseX - x));
            numValue.setValue(
                    (float) MathUtil.roundToPlace(diff / (double) width * (double) (numValue.getMax() - numValue.getMin())
                            + (double) numValue.getMin(), 2)
            );
        }

        String text = numValue.getName() + ": " + MathUtil.roundToPlace((double) numValue.getValue(), 2);
        mc.font.drawShadow(stack, text, (float) (x + 5), (float) (y + (height / 2 - 9 / 2)), -1);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(
                mouseX,
                mouseY,
                parent.getParent().getX(),
                parent.getParent().getY() + parent.getOffset() + offset,
                parent.getParent().getWidth(),
                parent.getParent().getHeight()
        ) && mouseButton == 0) {
            sliding = true;
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        sliding = false;
    }
}
