package com.shootforever.nuclear.ui.clickgui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.ui.clickgui.Component;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.ui.clickgui.ModuleRenderer;
import net.minecraft.client.Minecraft;

import java.util.List;

public class ChoiceValueComponent extends Component {
    private final Minecraft mc = Nuclear.mc;
    private final ChoiceValue choiceValue;

    public ChoiceValueComponent(ChoiceValue value, ModuleRenderer parent, int offset) {
        super(value, parent, offset);
        this.choiceValue = value;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        super.render(stack, mouseX, mouseY, delta, x, y, width, height);
        String text = this.choiceValue.getName() + ": " + this.choiceValue.getValue();
        mc.font.drawShadow(stack, text, (float) (x + 5), (float) (y + (height / 2 - 9 / 2)), -1);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isHovered(
                mouseX, mouseY, this.parent.parent.x, this.parent.parent.y + this.parent.offset + this.offset, this.parent.parent.width, this.parent.parent.height
        )
                && mouseButton == 0) {
            List<String> modes = this.choiceValue.getChoices();
            int currentIndex = modes.indexOf(this.choiceValue.getValue());
            int nextIndex = (currentIndex + 1) % modes.size();
            this.choiceValue.setValue(modes.get(nextIndex));
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
