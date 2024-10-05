package com.shootforever.nuclear.module.modules.player;

import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold", Category.PLAYER);
    }

    @Override
    protected void onEnable() {
        setEnabled(false);
    }
}
