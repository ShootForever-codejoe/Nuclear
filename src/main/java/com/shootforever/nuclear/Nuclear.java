package com.shootforever.nuclear;

import com.shootforever.nuclear.command.CommandManager;
import com.shootforever.nuclear.event.EventManager;
import com.shootforever.nuclear.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mod("nuclear")
public class Nuclear {
    public static final String name = "Nuclear";
    public static final String version = "1.0";
    public static final Minecraft mc = Minecraft.getInstance();
    public static final Path dataDir = Paths.get("%appdata%\\Nuclear");

    private static Nuclear instance;
    private final EventManager eventManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;

    static {
        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private Minecraft get_mc_Instance() {
        Minecraft minecraft = null;

        try {
            Class<?> classMinecraft = Class.forName("net.minecraft.client.Minecraft");

            for (Field field : classMinecraft.getDeclaredFields()) {
                if (field.getType() == classMinecraft) {
                    field.setAccessible(true);
                    minecraft = (Minecraft)field.get(null);
                    field.setAccessible(false);
                }
            }
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

        return minecraft;
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    public void setMinecraft(Minecraft minecraft) {
        this.minecraft = minecraft;
    }
}
