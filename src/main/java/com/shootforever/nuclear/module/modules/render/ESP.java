package com.shootforever.nuclear.module.modules.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.Render3DEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.ColorUtil;
import com.shootforever.nuclear.util.EntityUtil;
import com.shootforever.nuclear.util.RenderUtil;
import com.shootforever.nuclear.value.values.BooleanValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ESP extends Module {
    private final BooleanValue playersValue = new BooleanValue(this, "Players", true);
    private final BooleanValue mobsValue = new BooleanValue(this, "Mobs", false);
    private final BooleanValue animalsValue = new BooleanValue(this, "Animals", false);
    private final BooleanValue deadValue = new BooleanValue(this, "Dead", false);
    private final BooleanValue invisibleValue = new BooleanValue(this, "Invisible", false);

    public ESP() {
        super("ESP", Category.RENDER);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.level == null) return;
        PoseStack poseStack = event.getPoseStack();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof LivingEntity livingEntity) {
                if (EntityUtil.isSelected(
                        entity,
                        playersValue.getValue(),
                        mobsValue.getValue(),
                        animalsValue.getValue(),
                        deadValue.getValue(),
                        invisibleValue.getValue(),
                        true
                )) {
                    RenderUtil.renderEntityBoundingBox(poseStack, 0, livingEntity, ColorUtil.rainbow(10, 1).getRGB(), true);
                }
            }
        }
    }
}
