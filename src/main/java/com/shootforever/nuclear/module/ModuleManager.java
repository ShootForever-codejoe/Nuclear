package com.shootforever.nuclear.module;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.KeyPressEvent;
import com.shootforever.nuclear.module.combat.*;
import com.shootforever.nuclear.module.render.*;
import com.shootforever.nuclear.module.world.*;
import com.shootforever.nuclear.module.movement.*;
import com.shootforever.nuclear.module.misc.*;
import com.shootforever.nuclear.util.KeyboardUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManager {
    private final Map<String, Module> moduleMap = new ConcurrentHashMap<>();

    public ModuleManager() {
        registerModule(new HUD());
        registerModule(new ClickGUI());
        registerModule(new Scaffold());
        registerModule(new KillAura());

        getModule("ClickGUI").setKey(KeyboardUtil.getKeyNumber("rshift"));

        Nuclear.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void onKey(KeyPressEvent event) {
        for (Module module : getModules()) {
            if (module.getKey() != 0 && event.getKey() == module.getKey()) {
                module.setEnabled(!module.isEnabled());
            }
        }
    }

    private void registerModule(Module module) {
        moduleMap.put(module.getName(), module);
    }

    public Module getModule(String name) {
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public Collection<Module> getModules() {
        return moduleMap.values();
    }
}
