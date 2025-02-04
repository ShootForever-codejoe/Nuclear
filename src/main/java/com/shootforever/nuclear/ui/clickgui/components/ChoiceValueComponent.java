package com.shootforever.nuclear.ui.clickgui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.ui.clickgui.Component;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.ui.clickgui.ModuleRenderer;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChoiceValueComponent extends Component {
    private final Minecraft mc = Nuclear.mc;
    private final ChoiceValue choiceValue;

    public ChoiceValueComponent(ChoiceValue value, ModuleRenderer parent, int offset) {
        super(value, parent, offset);
        this.choiceValue = value;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        super.render(stack, mouseX, mouseY, delta, x, y, width, height);
        String text = choiceValue.getName() + ": " + choiceValue.getValue();
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
            List<String> modes = choiceValue.getChoices();
            int currentIndex = modes.indexOf(choiceValue.getValue());
            int nextIndex = (currentIndex + 1) % modes.size();
            choiceValue.setValue(modes.get(nextIndex));
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
