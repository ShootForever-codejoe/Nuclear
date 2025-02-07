package com.shootforever.nuclear.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.events.Render2DEvent;
import com.shootforever.nuclear.event.events.Render3DEvent;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V"))
    public void render(float p_109094_, long p_109095_, boolean p_109096_, CallbackInfo ci) {
        Render2DEvent event = new Render2DEvent();
        Nuclear.getInstance().getEventManager().call(event);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lcom/mojang/math/Matrix4f;)V"))
    public void renderLevel(float p_109090_, long p_109091_, PoseStack poseStack, CallbackInfo ci) {
        Render3DEvent event = new Render3DEvent(poseStack);
        Nuclear.getInstance().getEventManager().call(event);
    }
}
