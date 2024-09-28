package com.shootforever.nuclear;

import com.shootforever.nuclear.command.CommandManager;
import com.shootforever.nuclear.event.EventManager;
import com.shootforever.nuclear.event.events.LoadedEvent;
import com.shootforever.nuclear.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mod("nuclear")
public class Nuclear {
    public static final String CLIENT_NAME = "Nuclear";
    public static final String VERSION = "1.0";
    public static final Minecraft mc = Minecraft.getInstance();
    public static final Path DATA_DIR = Paths.get("%appdata%\\Nuclear");

    private static Nuclear instance;
    private final EventManager eventManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;

    static {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Nuclear() {
        instance = this;

        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        eventManager.call(new LoadedEvent());
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
