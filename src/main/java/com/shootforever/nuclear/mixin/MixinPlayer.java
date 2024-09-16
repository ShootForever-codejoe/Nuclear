package com.shootforever.nuclear.mixin;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.events.AttackEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getMobType()Lnet/minecraft/world/entity/MobType;"), cancellable = true)
    public void attack(Entity entity, CallbackInfo ci) {
        AttackEvent event = new AttackEvent(entity);
        Nuclear.getInstance().getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
