package com.shootforever.nuclear.module;

import com.shootforever.nuclear.Nuclear;
import net.minecraft.client.Minecraft;

public abstract class Module {
    protected static Minecraft mc = Minecraft.getInstance();

    private final String name;
    private final Category category;
    private int key;
    private boolean enabled;
    private final String description;

    protected Module(String name, Category category, int key, String description) {
        this.name = name;
        this.category = category;
        this.key = key;
        this.description = description;
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

    public String getDescription() {
        return description;
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
