package com.shootforever.nuclear.ui.clickgui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.ui.clickgui.Component;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.ui.clickgui.ModuleRenderer;
import net.minecraft.client.Minecraft;

public class BooleanValueComponent extends Component {
    private final Minecraft mc = Nuclear.mc;
    private final BooleanValue booleanValue;

    public BooleanValueComponent(BooleanValue value, ModuleRenderer parent, int offset) {
        super(value, parent, offset);
        this.booleanValue = value;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        super.render(stack, mouseX, mouseY, delta, x, y, width, height);
        String text = this.booleanValue.getName() + ": " + this.booleanValue.getValue();
        mc.font.drawShadow(stack, text, (float) (x + 5), (float) (y + (height / 2 - 9 / 2)), this.booleanValue.getValue() ? 5635925 : 16733525);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isHovered(
                mouseX, mouseY, this.parent.parent.x, this.parent.parent.y + this.parent.offset + this.offset, this.parent.parent.width, this.parent.parent.height
        )
                && mouseButton == 0) {
            this.booleanValue.setValue(!this.booleanValue.getValue());
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
