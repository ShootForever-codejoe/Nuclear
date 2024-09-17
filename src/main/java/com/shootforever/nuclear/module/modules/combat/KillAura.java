package com.shootforever.nuclear.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.shootforever.nuclear.event.events.MotionEvent;
import com.shootforever.nuclear.setting.Setting;
import com.shootforever.nuclear.setting.impl.BooleanSetting;
import com.shootforever.nuclear.setting.impl.NumberSetting;
import com.shootforever.nuclear.util.RotationUtils;
import com.shootforever.nuclear.util.TimerUtils;
import com.shootforever.nuclear.util.misc.EntityUtils;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import com.shootforever.nuclear.event.Event;
import com.shootforever.nuclear.event.events.annotations.EventPriority;
import com.shootforever.nuclear.event.events.LivingUpdateEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import net.minecraft.world.entity.LivingEntity;

public class KillAura extends Module {
    private final NumberSetting cpsValue = new NumberSetting("CPS", this, 11, 1, 20, 1);
    private final NumberSetting rangeValue = new NumberSetting("Range", this, 2.65, 1, 6.0, 0.01);
    private final BooleanSetting playersValue = new BooleanSetting("Players", this, true);
    private final BooleanSetting mobsValue = new BooleanSetting("Mobs", this, false);
    private final BooleanSetting animalsValue = new BooleanSetting("Animals", this, false);
    private final BooleanSetting deadValue = new BooleanSetting("Dead", this, false);
    private final BooleanSetting invisibleValue = new BooleanSetting("Invisible", this, false);
    private static LivingEntity target;
    private final List<LivingEntity> targets = new ArrayList<>();
    private final TimerUtils timer = new TimerUtils();

    public KillAura() {
        super("KillAura", Category.Combat, 82);
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

    @EventPriority
    public void onMotion(LivingUpdateEvent event) {
        List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                if (this.filter(livingEntity)) {
                    targets.add(livingEntity);
                }
            }
        }

        targets.sort(new Comparator<LivingEntity>() {
            public int compare(LivingEntity e1, LivingEntity e2) {
                return (int)(RotationUtils.getDistanceToEntityBox(e1) - RotationUtils.getDistanceToEntityBox(e2));
            }
        });
        target = null;
        if (!targets.isEmpty()) {
            target = targets.get(0);
        }

        if (target != null) {
            float[] rotations = RotationUtils.getSimpleRotations(target);
            mc.player.setYRot(rotations[0]);
            mc.player.setXRot(rotations[1]);
        }
    }

    @EventPriority
    public void onMotion(MotionEvent event) {
        if (event.getSide() == Event.Side.PRE && target != null && this.timer.delay((long)(800 / this.cpsValue.getValue().intValue()))) {
            mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(target, mc.player.isShiftKeyDown()));
            mc.player.swing(InteractionHand.MAIN_HAND);
            this.timer.reset();
        }
    }

    public boolean filter(LivingEntity entity) {
        if (RotationUtils.getDistanceToEntityBox(entity) > (double)this.rangeValue.getValue().floatValue()
                || !EntityUtils.isSelected(
                entity,
                this.playersValue.getValue(),
                this.mobsValue.getValue(),
                this.animalsValue.getValue(),
                this.deadValue.getValue(),
                this.invisibleValue.getValue(),
                true
        )) {
            return false;
        } else {
            return !mc.player.hasLineOfSight(entity) ? false : !entity.isDeadOrDying() && !(entity.getHealth() <= 0.0F);
        }
    }


    public static LivingEntity getTarget() {
        return target;
    }


    public List<LivingEntity> getTargets() {
        return this.targets;
    }

    @Override
    public ArrayList<Setting<?>> getSettings() {
        return super.getSettings();
    }
}
