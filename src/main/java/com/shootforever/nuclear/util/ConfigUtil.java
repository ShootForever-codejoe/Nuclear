package com.shootforever.nuclear.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.Value;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.value.values.NumberValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConfigUtil {
    public static String currentConfig = null;
    public static final Path configDir = Nuclear.dataDir.resolve("configs");
    public static final Path currentConfigPath = Nuclear.dataDir.resolve("currentConfig");

    static {
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getCurrentConfig();
    }

    private static void getCurrentConfig() {
        try {
            currentConfig = Files.readAllLines(currentConfigPath).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveConfig(String name) {
        currentConfig = name;
        try {
            Files.write(currentConfigPath, name.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path configPath = configDir.resolve(name + ".json");
        try {
            JsonObject config = new JsonObject();
            for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
                JsonObject moduleConfig = new JsonObject();
                for (Value<?> value : module.getValues()) {
                    if (value instanceof NumberValue numberValue) {
                        moduleConfig.addProperty(numberValue.getName(), numberValue.getValue());
                    } else if (value instanceof BooleanValue booleanValue) {
                        moduleConfig.addProperty(value.getName(), booleanValue.getValue());
                    } else if (value instanceof ChoiceValue choiceValue) {
                        moduleConfig.addProperty(value.getName(), choiceValue.getValue());
                    }
                }
                config.add(module.getName(), moduleConfig);
            }
            Files.write(configPath, config.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveConfig() {
        if (currentConfig == null) return false;
        return saveConfig(currentConfig);
    }
}
