package com.shootforever.nuclear.mixin;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.Event;
import com.shootforever.nuclear.event.events.GameTickEvent;
import com.shootforever.nuclear.event.events.ClientStartEvent;
import com.shootforever.nuclear.event.events.ClientStopEvent;
import com.shootforever.nuclear.event.events.WorldChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "run", at = @At("HEAD"))
    public void run(CallbackInfo ci) {
        ClientStartEvent event = new ClientStartEvent();
        Nuclear.getInstance().getEventManager().call(event);
    }

    @Inject(method = "stop", at = @At("HEAD"))
    public void stop(CallbackInfo ci) {
        ClientStopEvent event = new ClientStopEvent();
        Nuclear.getInstance().getEventManager().call(event);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraftforge/event/ForgeEventFactory;onPreClientTick()V",
            shift = At.Shift.BEFORE,
            ordinal = 0))
    public void preTick(CallbackInfo ci) {
        GameTickEvent event = new GameTickEvent(Event.Side.PRE);
        Nuclear.getInstance().getEventManager().call(event);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraftforge/event/ForgeEventFactory;onPostClientTick()V",
            shift = At.Shift.AFTER,
            ordinal = 0))
    public void postTick(CallbackInfo ci) {
        GameTickEvent event = new GameTickEvent(Event.Side.POST);
        Nuclear.getInstance().getEventManager().call(event);
    }
}
