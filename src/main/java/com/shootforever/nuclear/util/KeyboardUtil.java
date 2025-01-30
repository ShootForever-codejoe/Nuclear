package com.shootforever.nuclear.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class KeyboardUtil {
    private static final Map<String, Integer> keyMap = new HashMap<>();

    static {
        keyMap.put("NONE", 0);
        for (Field field : GLFW.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("GLFW_KEY_") && !field.getName().startsWith("GLFW_KEY_KP_")) {
                field.setAccessible(true);
                try {
                    keyMap.put(field.getName().substring(9).toUpperCase(), (Integer) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private KeyboardUtil() {
        throw new AssertionError();
    }

    public static @Nullable String getKeyName(int number) {
        for (String key : keyMap.keySet()) {
            if (number == keyMap.get(key)) {
                return key;
            }
        }
        return null;
    }

    public static Integer getKeyNumber(@NotNull String name) {
        for (String keyName : keyMap.keySet()) {
            name = name.replaceAll("_", "");
            if (name.equalsIgnoreCase(keyName.replaceAll("_", ""))
                    || ((keyName.startsWith("LEFT_") || keyName.startsWith("RIGHT_"))
                    && name.equalsIgnoreCase(keyName.replaceAll("LEFT_", "L").replaceAll("RIGHT_", "R").replaceAll("_", "")))) {
                return keyMap.get(keyName);
            }
        }
        return 0;
    }
}
