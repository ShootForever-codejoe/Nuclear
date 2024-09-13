package com.shootforever.nuclear.module;

import com.shootforever.nuclear.Nuclear;
import net.minecraft.client.Minecraft;

public abstract class Module {
    protected static Minecraft mc = Minecraft.getInstance();

    private final String name;
    private final Category category;
    private int key = 0;
    private boolean enabled;

    protected Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void onEnabled() {}
    protected void onDisabled() {}

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            Nuclear.getInstance().getEventManager().register(this);
            onEnabled();
        } else {
            Nuclear.getInstance().getEventManager().unregister(this);
            onDisabled();
        }
    }
}
