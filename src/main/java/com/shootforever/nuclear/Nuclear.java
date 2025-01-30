package com.shootforever.nuclear;

import com.shootforever.nuclear.command.CommandManager;
import com.shootforever.nuclear.event.EventManager;
import com.shootforever.nuclear.listener.ListenerManager;
import com.shootforever.nuclear.module.ModuleManager;
import com.shootforever.nuclear.util.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mod("nuclear")
public class Nuclear {
    public static final String CLIENT_NAME = "Nuclear";
    public static final String CLIENT_VERSION = "1.0";
    public static final Minecraft mc = Minecraft.getInstance();
    public static final Path DATA_DIR = Paths.get("Nuclear");

    private static Nuclear instance;
    private final EventManager eventManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;
    private final ListenerManager listenerManager;

    public Nuclear() {
        instance = this;

        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        listenerManager = new ListenerManager();

        ConfigUtil.init();
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

    public ListenerManager getListenerManager() {
        return listenerManager;
    }
}
