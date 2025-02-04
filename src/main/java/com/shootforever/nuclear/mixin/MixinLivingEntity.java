package com.shootforever.nuclear.mixin;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.events.JumpEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    @Shadow protected abstract float getJumpPower();

    @Shadow public abstract double getJumpBoostPower();

    public MixinLivingEntity() {
        //noinspection DataFlowIssue
        super(null, null);
    }

    /**
     * @author shootforever
     * @reason null
     */
    @Overwrite
    protected void jumpFromGround() {
        float angle = this.getYRot();

        if (Nuclear.mc.player != null && this.getId() == Nuclear.mc.player.getId()) {
            JumpEvent event = new JumpEvent(angle);
            Nuclear.getInstance().getEventManager().call(event);
            angle = event.getYaw();
        }

        double d0 = (double)this.getJumpPower() + this.getJumpBoostPower();
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, d0, vec3.z);
        if (this.isSprinting()) {
            float f = angle * ((float)Math.PI / 180F);
            this.setDeltaMovement(this.getDeltaMovement().add(-Mth.sin(f) * 0.2F, 0.0D, Mth.cos(f) * 0.2F));
        }

        this.hasImpulse = true;
        if (Nuclear.mc.level != null) {
            net.minecraftforge.common.ForgeHooks.onLivingJump((LivingEntity) Nuclear.mc.level.getEntity(getId()));
        }
    }
}
