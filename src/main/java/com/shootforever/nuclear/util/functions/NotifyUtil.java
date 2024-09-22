package com.shootforever.nuclear.util.functions;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public final class NotifyUtil {
    private NotifyUtil() {
        throw new AssertionError();
    }

    public static void notifyAsMessage(@NotNull String content) {
        Minecraft.getInstance().gui.getChat().addMessage(new TextComponent(ChatFormatting.GOLD + "[Nuclear] " + ChatFormatting.WHITE + content));
    }
}
