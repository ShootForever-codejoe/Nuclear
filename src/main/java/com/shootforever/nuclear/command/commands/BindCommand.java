package com.shootforever.nuclear.command.commands;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.command.Command;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.NotifyUtil;
import net.minecraft.ChatFormatting;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class BindCommand extends Command {
    private final Map<String, Integer> keyMap = new HashMap<>();

    public BindCommand() {
        super("bind");

        keyMap.put("none", 0);
        for (Field field : GLFW.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("GLFW_KEY_")) {
                field.setAccessible(true);
                try {
                    keyMap.put(field.getName().substring(9).toUpperCase(), (Integer) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "用法: .bind <module> <key>");
            return;
        }

        Module module = Nuclear.getInstance().getModuleManager().getModule(args[0]);
        if (module == null) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "模块" + args[0] + "不存在");
            return;
        }

        Integer key = keyMap.get(args[1]);
        if (key == null) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED  + args[1].toUpperCase() + "键不存在");
            return;
        }

        module.setKey(key);
        NotifyUtil.notifyAsMessage("模块" + args[0] + "成功绑定为" + args[1].toUpperCase() + "键");
    }

    public String getKeyName(int number) {
        for (String key : keyMap.keySet()) {
            if (number == keyMap.get(key)) {
                return key.toUpperCase();
            }
        }
        return null;
    }

    public int getKeyNumber(String name) {
        return keyMap.get(name.toUpperCase());
    }
}
