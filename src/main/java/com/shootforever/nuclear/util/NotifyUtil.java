package com.shootforever.nuclear.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;

public class NotifyUtil {
    public static void notifyAsMessage(String content) {
        Minecraft.getInstance().gui.getChat().addMessage(new TextComponent(ChatFormatting.GOLD + "[Nuclear] " + ChatFormatting.WHITE + content));
    }
}
