package com.shootforever.nuclear.module;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.KeyPressEvent;
import com.shootforever.nuclear.module.modules.combat.*;
import com.shootforever.nuclear.module.modules.misc.*;
import com.shootforever.nuclear.module.modules.movement.*;
import com.shootforever.nuclear.module.modules.render.*;
import com.shootforever.nuclear.module.modules.player.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        registerModules(
                // Combat
                new KillAura(),

                // Movement
                new Sprint(),
                new MoveFix(),

                // Render
                new HUD(),
                new ClickGUI(),

                // Player
                new Eagle(),
                new Scaffold(),

                // Misc
                new IRC()
        );

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

    private void registerModules(Module... modules) {
        for (Module module : modules) {
            registerModule(module);
        }
    }

    private void registerModule(Module module) {
        modules.add(module);
    }

    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }
}
