package com.shootforever.nuclear.mixin;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.Event;
import com.shootforever.nuclear.event.events.ChatEvent;
import com.shootforever.nuclear.event.events.MotionUpdateEvent;
import com.shootforever.nuclear.event.events.PlayerTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer {
    @Shadow private boolean wasSprinting;

    @Shadow @Final public ClientPacketListener connection;

    @Shadow private boolean wasShiftKeyDown;

    @Shadow protected abstract boolean isControlledCamera();

    @Shadow private double xLast;

    @Shadow private double yLast1;

    @Shadow private double zLast;

    @Shadow private float yRotLast;

    @Shadow private int positionReminder;

    @Shadow private float xRotLast;

    @Shadow private boolean lastOnGround;

    @Shadow private boolean autoJumpEnabled;

    @Shadow @Final protected Minecraft minecraft;

    @Shadow @Final private List<AmbientSoundHandler> ambientSoundHandlers;

    public MixinLocalPlayer() {
        super(null, null);
    }

    /**
     * @author shootforever
     * @reason null
     */
    @Overwrite
    private void sendPosition() {
        MotionUpdateEvent event = new MotionUpdateEvent(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot(), this.onGround);
        Nuclear.getInstance().getEventManager().call(event);

        boolean flag = this.isSprinting();
        if (flag != this.wasSprinting) {
            ServerboundPlayerCommandPacket.Action serverboundplayercommandpacket$action = flag ? ServerboundPlayerCommandPacket.Action.START_SPRINTING : ServerboundPlayerCommandPacket.Action.STOP_SPRINTING;
            this.connection.send(new ServerboundPlayerCommandPacket(this, serverboundplayercommandpacket$action));
            this.wasSprinting = flag;
        }

        boolean flag3 = this.isShiftKeyDown();
        if (flag3 != this.wasShiftKeyDown) {
            ServerboundPlayerCommandPacket.Action serverboundplayercommandpacket$action1 = flag3 ? ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY : ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY;
            this.connection.send(new ServerboundPlayerCommandPacket(this, serverboundplayercommandpacket$action1));
            this.wasShiftKeyDown = flag3;
        }

        if (this.isControlledCamera()) {
            double d4 = event.getX() - this.xLast;
            double d0 = event.getY() - this.yLast1;
            double d1 = event.getZ() - this.zLast;
            double d2 = this.getYRot() - this.yRotLast;
            double d3 = this.getXRot() - this.xRotLast;
            ++this.positionReminder;
            boolean flag1 = d4 * d4 + d0 * d0 + d1 * d1 > 9.0E-4D || this.positionReminder >= 20;
            boolean flag2 = d2 != 0.0D || d3 != 0.0D;
            if (this.isPassenger()) {
                Vec3 vec3 = this.getDeltaMovement();
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(vec3.x, -999.0D, vec3.z, event.getYaw(), event.getPitch(), event.isOnGround()));
                flag1 = false;
            } else if (flag1 && flag2) {
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (flag1) {
                this.connection.send(new ServerboundMovePlayerPacket.Pos(event.getX(), event.getY(), event.getZ(), event.isOnGround()));
            } else if (flag2) {
                this.connection.send(new ServerboundMovePlayerPacket.Rot(event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (this.lastOnGround != this.onGround) {
                this.connection.send(new ServerboundMovePlayerPacket.StatusOnly(event.isOnGround()));
            }

            if (flag1) {
                this.xLast = event.getX();
                this.yLast1 = event.getY();
                this.zLast = event.getZ();
                this.positionReminder = 0;
            }

            if (flag2) {
                this.yRotLast = event.getYaw();
                this.xRotLast = event.getPitch();
            }

            this.lastOnGround = event.isOnGround();
            this.autoJumpEnabled = this.minecraft.options.autoJump;
        }
    }

    @Inject(method = "chat", at = @At("HEAD"), cancellable = true)
    public void chat(String message, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(message);
        Nuclear.getInstance().getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
            shift = At.Shift.BEFORE,
            ordinal = 0))
    public void preTick(CallbackInfo ci) {
        PlayerTickEvent event = new PlayerTickEvent(Event.Side.PRE);
        Nuclear.getInstance().getEventManager().call(event);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
            shift = At.Shift.AFTER,
            ordinal = 0))
    public void postTick(CallbackInfo ci) {
        PlayerTickEvent event = new PlayerTickEvent(Event.Side.POST);
        Nuclear.getInstance().getEventManager().call(event);
    }
}
