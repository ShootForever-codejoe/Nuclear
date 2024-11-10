package com.shootforever.nuclear.module.modules.combat;

import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.GameTickEvent;
import com.shootforever.nuclear.event.events.MotionUpdateEvent;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.NumberValue;
import com.shootforever.nuclear.util.functions.RotationUtil;
import com.shootforever.nuclear.util.classes.Timer;
import com.shootforever.nuclear.util.functions.EntityUtil;
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
    private final Timer timer = new Timer();

    public KillAura() {
        super("KillAura", Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        targets.clear();
        target = null;
    }

    @Override
    protected void onDisable() {
        targets.clear();
        target = null;
    }

    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (mc.level == null || mc.player == null) return;

        List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof LivingEntity livingEntity) {
                if (filter(livingEntity)) {
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
            RotationUtil.setAngle(rotations[0], rotations[1]);
        }
    }

    @EventTarget
    public void onMotion(GameTickEvent event) {
        if (mc.getConnection() == null || mc.player == null) return;

        if (event.getSide() == Event.Side.PRE && target != null && timer.delay(800 / cps.getValue().intValue())) {
            mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(target, mc.player.isShiftKeyDown()));
            mc.player.swing(InteractionHand.MAIN_HAND);
            timer.reset();
        }
    }

    public boolean filter(LivingEntity entity) {
        if (mc.player == null) return false;

        if (RotationUtil.getDistanceToEntityBox(entity) > range.getValue()
                || !EntityUtil.isSelected(
                    entity,
                    players.getValue(),
                    mobs.getValue(),
                    animals.getValue(),
                    dead.getValue(),
                    invisible.getValue(),
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
        return targets;
    }
}
