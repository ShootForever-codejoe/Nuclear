package com.shootforever.nuclear.module.modules.render;

import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.KeyboardUtil;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Category.Render);
        key = KeyboardUtil.getKeyNumber("INSERT");
    }
}
