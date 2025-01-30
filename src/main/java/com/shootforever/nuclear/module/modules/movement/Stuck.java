package com.shootforever.nuclear.module.modules.movement;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.GameTickEvent;
import com.shootforever.nuclear.event.events.PacketEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.phys.Vec3;

public class Stuck extends Module {
    public Stuck() {
        super("Stuck", Category.MOVEMENT);
    }

    @EventTarget
    public void onTick(GameTickEvent event) {
        if (mc.player == null) return;

        Vec3 deltaMovement = mc.player.getDeltaMovement();
        mc.player.setDeltaMovement(deltaMovement.x * 0, deltaMovement.y * 0, deltaMovement.z * 0);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof ClientboundMoveEntityPacket) {
            event.setCancelled(true);
        }
    }
}
