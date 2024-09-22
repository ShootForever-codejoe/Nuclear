package com.shootforever.nuclear.module.modules.combat;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.MotionUpdateEvent;
import com.shootforever.nuclear.event.events.PacketEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.functions.ReflectionUtil;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class Velocity extends Module {
    private final @NotNull KillAura killAura = (KillAura) Nuclear.getInstance().getModuleManager().getModule("KillAura");
    public boolean receivedKnockBack;
    public boolean attacked;

    public Velocity() {
        super("Velocity", Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        receivedKnockBack = attacked = false;
    }

    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (mc.player == null || mc.getConnection() == null) return;

        if (mc.player.hurtTime == 0) {
            receivedKnockBack = attacked = false;
        }

        if (receivedKnockBack && !attacked) {
            boolean sprinting = false;
            Field sprintingField = ReflectionUtil.findField(mc.player.getClass(), "f_108603_", "wasSprinting");

            try {
                sprinting = sprintingField.getBoolean(mc.player.isSprinting());
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            if (!sprinting) {
                mc.getConnection().getConnection().send(new ServerboundPlayerCommandPacket(mc.player, Action.START_SPRINTING));
            }

            for (int i = 0; i < 5; i++) {
                mc.getConnection().getConnection().send(ServerboundInteractPacket.createAttackPacket(killAura.getTarget(), mc.player.isShiftKeyDown()));
                mc.getConnection().getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
            }

            if (!sprinting) {
                mc.getConnection().getConnection().send(new ServerboundPlayerCommandPacket(mc.player, Action.STOP_SPRINTING));
            }

            attacked = true;
            Vec3 deltaMovement = mc.player.getDeltaMovement();
            mc.player.setDeltaMovement(deltaMovement.x * 0.07776, deltaMovement.y, deltaMovement.z * 0.07776);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (mc.player != null
                && event.getPacket() instanceof ClientboundSetEntityMotionPacket packet
                && packet.getId() == mc.player.getId()
                && killAura.getTarget() != null) {
            attacked = false;
            receivedKnockBack = true;
        }
    }
}
