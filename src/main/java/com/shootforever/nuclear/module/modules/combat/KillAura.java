package com.shootforever.nuclear.module.modules.combat;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.GameTickEvent;
import com.shootforever.nuclear.event.events.MotionUpdateEvent;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.NumberValue;
import com.shootforever.nuclear.util.RotationUtil;
import com.shootforever.nuclear.util.TimerUtil;
import com.shootforever.nuclear.util.EntityUtil;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import com.shootforever.nuclear.event.Event;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class KillAura extends Module {
    private final NumberValue cps = new NumberValue(this, "CPS", 11, 1, 20, 1);
    private final NumberValue range = new NumberValue(this, "Range", 2.65f, 1, 6.0f, 0.01f);
    private final BooleanValue players = new BooleanValue(this, "Players", true);
    private final BooleanValue mobs = new BooleanValue(this, "Mobs", false);
    private final BooleanValue animals = new BooleanValue(this, "Animals", false);
    private final BooleanValue dead = new BooleanValue(this, "Dead", false);
    private final BooleanValue invisible = new BooleanValue(this, "Invisible", false);
    
    private LivingEntity target;
    private final List<LivingEntity> targets = new ArrayList<>();
    private final TimerUtil timer = new TimerUtil();

    public KillAura() {
        super("KillAura", Category.Combat);
    }

    @Override
    protected void onEnable() {
        this.targets.clear();
        target = null;
    }

    @Override
    protected void onDisable() {
        this.targets.clear();
        target = null;
    }

    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (mc.level == null || mc.player == null) return;

        List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof LivingEntity livingEntity) {
                if (this.filter(livingEntity)) {
                    targets.add(livingEntity);
                }
            }
        }

        targets.sort((e1, e2) -> (int) (RotationUtil.getDistanceToEntityBox(e1) - RotationUtil.getDistanceToEntityBox(e2)));
        target = null;
        if (!targets.isEmpty()) {
            target = targets.get(0);
        }

        if (target != null) {
            float[] rotations = RotationUtil.getSimpleRotations(target);
            mc.player.setYRot(rotations[0]);
            mc.player.setXRot(rotations[1]);
        }
    }

    @EventTarget
    public void onMotion(GameTickEvent event) {
        if (mc.getConnection() == null || mc.player == null) return;

        if (event.getSide() == Event.Side.PRE && target != null && this.timer.delay(800 / this.cps.getValue().intValue())) {
            mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(target, mc.player.isShiftKeyDown()));
            mc.player.swing(InteractionHand.MAIN_HAND);
            this.timer.reset();
        }
    }

    public boolean filter(LivingEntity entity) {
        if (mc.player == null) return false;

        if (RotationUtil.getDistanceToEntityBox(entity) > this.range.getValue()
                || !EntityUtil.isSelected(
                    entity,
                    this.players.getValue(),
                    this.mobs.getValue(),
                    this.animals.getValue(),
                    this.dead.getValue(),
                    this.invisible.getValue(),
                    true
                )) {
            return false;
        } else {
            return mc.player.hasLineOfSight(entity) && !entity.isDeadOrDying() && !(entity.getHealth() <= 0.0F);
        }
    }

    public LivingEntity getTarget() {
        return target;
    }

    public List<LivingEntity> getTargets() {
        return this.targets;
    }
}
