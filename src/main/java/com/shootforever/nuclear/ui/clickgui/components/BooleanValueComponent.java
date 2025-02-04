package com.shootforever.nuclear.ui.clickgui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.ui.clickgui.Component;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.ui.clickgui.ModuleRenderer;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class BooleanValueComponent extends Component {
    private final Minecraft mc = Nuclear.mc;
    private final BooleanValue booleanValue;

    public BooleanValueComponent(BooleanValue value, ModuleRenderer parent, int offset) {
        super(value, parent, offset);
        booleanValue = value;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        super.render(stack, mouseX, mouseY, delta, x, y, width, height);
        String text = booleanValue.getName() + ": " + booleanValue.getValue();
        mc.font.drawShadow(stack, text, (float) (x + 5), (float) (y + (height / 2 - 9 / 2)), booleanValue.getValue() ? 5635925 : 16733525);
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
            booleanValue.setValue(!booleanValue.getValue());
        }
    }
}
