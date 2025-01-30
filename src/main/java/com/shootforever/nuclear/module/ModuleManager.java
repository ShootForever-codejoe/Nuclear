package com.shootforever.nuclear.module;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.KeyPressEvent;
import com.shootforever.nuclear.module.modules.combat.*;
import com.shootforever.nuclear.module.modules.misc.*;
import com.shootforever.nuclear.module.modules.movement.*;
import com.shootforever.nuclear.module.modules.render.*;
import com.shootforever.nuclear.module.modules.player.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<@NotNull Module> modules = new ArrayList<>();

    public ModuleManager() {
        registerModules(
                // Combat
                new KillAura(),
                new Velocity(),

                // Movement
                new Sprint(),
                new MoveFix(),
                new NoSlow(),

                // Render
                new HUD(),
                new ClickGUI(),

                // Player
                new Eagle(),
                //new Scaffold(),

                // Misc
                //new IRC(),
                new Team()
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

    private void registerModules(@NotNull Module @NotNull ... modules) {
        for (Module module : modules) {
            registerModule(module);
        }
    }

    private void registerModule(@NotNull Module module) {
        modules.add(module);
    }

    public @Nullable Module getModule(@NotNull String name) {
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
