package com.shootforever.nuclear.mixin;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.events.StrafeEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow public abstract void setDeltaMovement(Vec3 p_20257_);

    @Shadow public abstract Vec3 getDeltaMovement();

    @SuppressWarnings("SameReturnValue")
    @Shadow
    private static Vec3 getInputVector(Vec3 p_20016_, float p_20017_, float p_20018_) {
        return null;
    }

    @Shadow public abstract float getYRot();

    @Shadow public abstract int getId();

    /**
     * @author shootforever
     * @reason null
     */
    @Overwrite
    public void moveRelative(float p_19921_, Vec3 p_19922_) {
        Vec3 vec3;
        if (Nuclear.mc.player != null && this.getId() == Nuclear.mc.player.getId()) {
            StrafeEvent event = new StrafeEvent(this.getYRot());
            Nuclear.getInstance().getEventManager().call(event);
            vec3 = getInputVector(p_19922_, p_19921_, event.getYaw());
        } else {
            vec3 = getInputVector(p_19922_, p_19921_, this.getYRot());
        }
        assert vec3 != null;
        this.setDeltaMovement(this.getDeltaMovement().add(vec3));
    }
}
