package com.shootforever.nuclear.mixin;

import com.shootforever.nuclear.event.events.KeyPressEvent;
import com.shootforever.nuclear.Nuclear;
import net.minecraft.client.KeyboardHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandler {
    @Inject(method = "keyPress", at = @At("HEAD"))
    public void keyPress(long p_90894_, int key, int p_90896_, int action, int p_90898_, CallbackInfo ci) {
        if (action == GLFW.GLFW_PRESS && key != GLFW.GLFW_KEY_UNKNOWN && Nuclear.mc.screen == null) {
            KeyPressEvent event = new KeyPressEvent(key);
            Nuclear.getInstance().getEventManager().call(event);
        }
    }
}
