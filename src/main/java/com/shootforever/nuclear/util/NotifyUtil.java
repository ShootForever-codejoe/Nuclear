package com.shootforever.nuclear.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public final class NotifyUtil {
    private NotifyUtil() {
        throw new AssertionError();
    }

    public static void notifyAsMessage(@NotNull String @NotNull ... contents) {
        for (String content : contents) {
            Minecraft.getInstance().gui.getChat().addMessage(new TextComponent(ChatFormatting.GOLD + "[Nuclear] " + ChatFormatting.WHITE + content));
        }
    }
}
