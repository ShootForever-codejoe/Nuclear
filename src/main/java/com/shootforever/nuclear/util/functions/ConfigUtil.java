package com.shootforever.nuclear.util.functions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.value.Value;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.value.values.NumberValue;
import net.minecraft.ChatFormatting;
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

        if (currentConfig != null) {
            if (!loadConfig(currentConfig)) {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "读取配置" + currentConfig + "失败");
            }
        }
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

    public static boolean loadConfig(@NotNull String name) {
        Path configPath = CONFIG_DIR.resolve(name + ".json");
        if (!Files.exists(configPath)) {
            return false;
        }

        JsonObject config;
        try {
            config = JsonParser.parseString(String.join("\n", Files.readAllLines(configPath))).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        for (String moduleName : config.keySet()) {
            Module module = Nuclear.getInstance().getModuleManager().getModule(moduleName);
            if (module == null) continue;
            JsonObject moduleConfig = config.get(moduleName).getAsJsonObject();
            for (String valueName : moduleConfig.keySet()) {
                JsonElement newValue = moduleConfig.get(valueName);

                if (valueName.equalsIgnoreCase("Enabled")) {
                    module.setEnabled(newValue.getAsBoolean());
                    continue;
                }

                if (valueName.equalsIgnoreCase("Bind")) {
                    module.setKey(newValue.getAsInt());
                    continue;
                }

                Value<?> value = module.getValue(valueName);
                if (value == null) continue;
                if (value instanceof NumberValue numberValue) {
                    numberValue.setValue(newValue.getAsFloat());
                } else if (value instanceof BooleanValue booleanValue) {
                    booleanValue.setValue(newValue.getAsBoolean());
                } else if (value instanceof ChoiceValue choiceValue) {
                    choiceValue.setValue(newValue.getAsString());
                }
            }
        }

        return true;
    }

    public static boolean saveConfig(@NotNull String name) {
        currentConfig = name;
        try {
            Files.write(CURRENT_CONFIG_PATH, name.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject config = new JsonObject();
        for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
            JsonObject moduleConfig = new JsonObject();
            moduleConfig.addProperty("Enabled", module.isEnabled());
            moduleConfig.addProperty("Bind", module.getKey());
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

        Path configPath = CONFIG_DIR.resolve(name + ".json");
        try {
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
