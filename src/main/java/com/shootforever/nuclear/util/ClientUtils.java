package com.shootforever.nuclear.util;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.util.wrapper.Wrapper;
import net.minecraft.network.chat.TextComponent;


public class ClientUtils implements Wrapper {
    public static void mc_debugMessage(String debugMessage) {
        if (mc.level != null && mc.player != null) {
            mc.gui.getChat().addMessage(new TextComponent("§8[§c§l" + Nuclear.name + "§8]§c§d" + debugMessage));
        }
    }

    public static void displayIRC(String text) {
        mc_debugMessage("§l[§bNuclear§r§l] §r" + text);
    }
}
