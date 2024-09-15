package com.shootforever.nuclear.module.modules.render;

import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import net.minecraft.network.chat.TextComponent;

import java.util.UUID;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Category.Render);
    }

    @Override
    protected void onEnabled() {
        if (mc.player == null) return;
        mc.player.sendMessage(new TextComponent("test"), UUID.randomUUID());
    }
}
