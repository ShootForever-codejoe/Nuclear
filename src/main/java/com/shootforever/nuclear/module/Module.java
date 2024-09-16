package com.shootforever.nuclear.module;


import java.util.ArrayList;
import java.util.List;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.setting.Setting;
import com.shootforever.nuclear.util.ClientUtils;
import com.shootforever.nuclear.util.wrapper.Wrapper;
import com.shootforever.nuclear.value.Value;

public abstract class Module implements Wrapper {
    private final String name;
    private final Category category;
    private final ArrayList<Setting<?>> settings;
    private int key;
    private boolean enabled;
    private String suffix;
    private final List<Value<?>> values = new ArrayList<>();


    public Module(String name, Category category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
        this.settings = new ArrayList<Setting<?>>();
    }

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.key = 0;
        this.settings = new ArrayList<Setting<?>>();
    }

    public Setting<?> findSetting(String name) {
        for (Setting<?> setting : this.getSettings()) {
            if (setting.getName().replace(" ", "").equalsIgnoreCase(name)) {
                return setting;
            }
        }

        return null;
    }

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            ClientUtils.mc_debugMessage("Module " + this.name + " enable!");
            this.onEnable();
            Nuclear.INSTANCE.getEventManager().register(this);
        } else {
            ClientUtils.mc_debugMessage("Module " + this.name + " disable!");
            Nuclear.INSTANCE.getEventManager().unregister(this);
            this.onDisable();
        }
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }


    public String getName() {
        return this.name;
    }


    public Category getCategory() {
        return this.category;
    }


    public ArrayList<Setting<?>> getSettings() {
        return this.settings;
    }


    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<Value<?>> getValues() {
        return new ArrayList<>(values);
    }

    public Value<?> getValue(String name) {
        for (Value<?> value : values) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public void registerValue(Value<?> value) {
        values.add(value);
    }
}

