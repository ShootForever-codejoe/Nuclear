package com.shootforever.nuclear.module.modules.render;

import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.ui.clickgui.ClickGUIScreen;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        mc.setScreen(ClickGUIScreen.getInstance());
        setEnabled(false);
    }
}
