package com.shootforever.nuclear.util.functions;

import com.google.gson.JsonObject;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.Value;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.value.values.NumberValue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class ConfigUtil {
    public static String currentConfig = null;
    public static final Path CONFIG_DIR = Nuclear.DATA_DIR.resolve("configs");
    public static final Path CURRENT_CONFIG_PATH = Nuclear.DATA_DIR.resolve("currentConfig");

    static {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getCurrentConfig();
    }

    private ConfigUtil() {
        throw new AssertionError();
    }

    private static void getCurrentConfig() {
        try {
            currentConfig = Files.readAllLines(CURRENT_CONFIG_PATH).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveConfig(@NotNull String name) {
        currentConfig = name;
        try {
            Files.write(CURRENT_CONFIG_PATH, name.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path configPath = CONFIG_DIR.resolve(name + ".json");
        try {
            JsonObject config = new JsonObject();
            for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
                JsonObject moduleConfig = new JsonObject();
                moduleConfig.addProperty("Enabled", module.isEnabled());
                moduleConfig.addProperty("Key", module.getKey());
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
