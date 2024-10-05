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
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class ConfigUtil {
    private static @Nullable String currentConfig = null;
    public static final Path CONFIG_DIR = Nuclear.DATA_DIR.resolve("configs");
    public static final Path CURRENT_CONFIG_PATH = Nuclear.DATA_DIR.resolve("currentConfig");

    private ConfigUtil() {
        throw new AssertionError();
    }

    public static void init() {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadCurrentConfig();

        if (currentConfig != null) {
            try {
                loadConfig(currentConfig);
            } catch (IOException e) {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "读取配置" + currentConfig + "失败");
            }
        } else if (Files.exists(CONFIG_DIR.resolve("default.json"))) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "配置default已存在，无法自动创建新配置");
        } else {
            try {
                saveConfig("default");
            } catch (IOException e) {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "自动创建新配置default失败");
            }
        }
    }

    private static void loadCurrentConfig() {
        try {
            currentConfig = Files.readAllLines(CURRENT_CONFIG_PATH).get(0);
        } catch (IOException e) {
            e.printStackTrace();
            currentConfig = null;
        }
    }

    public static void loadConfig(@NotNull String name) throws IOException {
        Path configPath = CONFIG_DIR.resolve(name + ".json");
        if (!Files.exists(configPath)) {
            throw new FileNotFoundException(configPath.toString());
        }

        currentConfig = name;
        try {
            Files.write(CURRENT_CONFIG_PATH, name.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject config;
        config = JsonParser.parseString(String.join("", Files.readAllLines(configPath))).getAsJsonObject();

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
    }

    public static void loadConfig() throws IOException {
        if (currentConfig == null) throw new IOException("Current config is null");
        loadConfig(currentConfig);
    }

    public static void saveConfig(@NotNull String name) throws IOException {
        currentConfig = name;
        try {
            Files.write(CURRENT_CONFIG_PATH, name.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
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
        Files.write(configPath, config.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void saveConfig() throws IOException {
        if (currentConfig == null) throw new IOException("Current config is null");
        saveConfig(currentConfig);
    }

    public static @Nullable String getCurrentConfig() {
        return currentConfig;
    }
}
