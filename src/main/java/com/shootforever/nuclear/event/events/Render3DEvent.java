package com.shootforever.nuclear.event.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.shootforever.nuclear.event.Event;
import org.jetbrains.annotations.NotNull;

public class Render3DEvent extends Event {
    private final PoseStack poseStack;

    public Render3DEvent(@NotNull PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }
}
