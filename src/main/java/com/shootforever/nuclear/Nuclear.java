package com.shootforever.nuclear;

import com.shootforever.nuclear.command.CommandManager;
import com.shootforever.nuclear.event.EventManager;
import com.shootforever.nuclear.module.ModuleManager;
import net.minecraftforge.fml.common.Mod;

@Mod("nuclear")
public class Nuclear {
    public static final String name = "Nuclear";
    public static final String version = "1.0";

    private static Nuclear instance;

    private final EventManager eventManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;

    public Nuclear() {
        instance = this;

        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
    }

    public static Nuclear getInstance() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
