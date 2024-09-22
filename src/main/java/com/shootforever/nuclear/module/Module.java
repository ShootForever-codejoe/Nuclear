package com.shootforever.nuclear.module;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.value.Value;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected final Minecraft mc = Minecraft.getInstance();
    protected final @NotNull String name;
    protected final @NotNull Category category;
    private int key = 0;
    private boolean enabled;
    private final List<@NotNull Value<?>> values = new ArrayList<>();

    protected Module(@NotNull String name, @NotNull Category category) {
        this.name = name;
        this.category = category;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Category getCategory() {
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

    protected void onEnable() {}
    protected void onDisable() {}

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            Nuclear.getInstance().getEventManager().register(this);
            onEnable();
        } else {
            Nuclear.getInstance().getEventManager().unregister(this);
            onDisable();
        }
    }

    public List<Value<?>> getValues() {
        return new ArrayList<>(values);
    }

    public @Nullable Value<?> getValue(@NotNull String name) {
        for (Value<?> value : values) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public void registerValue(@NotNull Value<?> value) {
        values.add(value);
    }
}
