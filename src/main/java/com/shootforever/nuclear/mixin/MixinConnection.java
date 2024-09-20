package com.shootforever.nuclear.mixin;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.Event;
import com.shootforever.nuclear.event.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class MixinConnection extends SimpleChannelInboundHandler<Packet<?>> {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void send(Packet<?> packet, CallbackInfo ci) {
        PacketEvent event = new PacketEvent(Event.Side.PRE, packet);
        Nuclear.getInstance().getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        PacketEvent event = new PacketEvent(Event.Side.POST, packet);
        Nuclear.getInstance().getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
