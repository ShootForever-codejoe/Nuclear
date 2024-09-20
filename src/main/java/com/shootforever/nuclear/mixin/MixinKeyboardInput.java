package com.shootforever.nuclear.mixin;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.events.KeyboardInputEvent;
import com.shootforever.nuclear.event.events.MovementInputEvent;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyboardInput.class)
public abstract class MixinKeyboardInput extends Input {
    @Shadow @Final private Options options;

    /**
     * @author shootforever
     * @reason null
     */
    @Overwrite
    public void tick(boolean p_108582_) {
        this.up = this.options.keyUp.isDown();
        this.down = this.options.keyDown.isDown();
        this.left = this.options.keyLeft.isDown();
        this.right = this.options.keyRight.isDown();
        this.forwardImpulse = this.up == this.down ? 0.0F : (this.up ? 1.0F : -1.0F);
        this.leftImpulse = this.left == this.right ? 0.0F : (this.left ? 1.0F : -1.0F);

        MovementInputEvent movementInputEvent = new MovementInputEvent(forwardImpulse, leftImpulse);
        Nuclear.getInstance().getEventManager().call(movementInputEvent);
        forwardImpulse = movementInputEvent.getForward();
        leftImpulse = movementInputEvent.getStrafe();

        this.jumping = this.options.keyJump.isDown();
        this.shiftKeyDown = this.options.keyShift.isDown();
        if (p_108582_) {
            this.leftImpulse = (float)((double)this.leftImpulse * 0.3D);
            this.forwardImpulse = (float)((double)this.forwardImpulse * 0.3D);
        }

        KeyboardInputEvent keyboardInputEvent = new KeyboardInputEvent();
        Nuclear.getInstance().getEventManager().call(keyboardInputEvent);
        if (keyboardInputEvent.isCancelled()) {
            super.forwardImpulse *= 5f;
            super.leftImpulse *= 5f;
        }
    }
}
