package com.shootforever.nuclear.command.commands;

import com.shootforever.nuclear.command.Command;
import com.shootforever.nuclear.util.ConfigUtil;
import com.shootforever.nuclear.util.NotifyUtil;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config");
    }

    private void notifyUsage() {
        NotifyUtil.notifyAsMessage(
                ChatFormatting.RED + "用法:",
                ChatFormatting.RED + "(1) .config load <config_name>",
                ChatFormatting.RED + "(2) .config save [<config_name>]",
                ChatFormatting.RED + "(3) .config dir"
        );
    }

    @Override
    public void execute(@NotNull String @NotNull [] args) {
        if (args.length == 0) {
            notifyUsage();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "load":
                if (args.length != 2) {
                    notifyUsage();
                    break;
                }

                try {
                    ConfigUtil.loadConfig(args[1]);
                } catch (FileNotFoundException e) {
                    NotifyUtil.notifyAsMessage(ChatFormatting.RED + "配置" + args[1] + "不存在");
                } catch (IOException e) {
                    NotifyUtil.notifyAsMessage(ChatFormatting.RED + "加载配置" + args[1] + "失败");
                }

                NotifyUtil.notifyAsMessage("加载配置" + args[1] + "成功");
                break;

            case "save":
                String configName;
                if (args.length == 1) {
                    configName = ConfigUtil.getCurrentConfig();
                    if (configName == null) {
                        NotifyUtil.notifyAsMessage(ChatFormatting.RED + "目前的配置无名，请使用.config save <config_name>");
                        break;
                    }
                } else if (args.length == 2) {
                    configName = args[1];
                } else {
                    notifyUsage();
                    break;
                }

                try {
                    ConfigUtil.saveConfig(configName);
                } catch (IOException e) {
                    NotifyUtil.notifyAsMessage(ChatFormatting.RED + "保存配置" + configName + "失败");
                }

                NotifyUtil.notifyAsMessage("保存配置" + configName + "成功");
                break;

            case "dir":
                if (args.length != 1) {
                    notifyUsage();
                    break;
                }

                if (!Desktop.isDesktopSupported()) {
                    NotifyUtil.notifyAsMessage(ChatFormatting.RED + "不支持打开文件夹");
                    break;
                }

                File configDir = ConfigUtil.CONFIG_DIR.toFile();

                if (!configDir.exists()) {
                    if (!configDir.mkdirs()) {
                        NotifyUtil.notifyAsMessage(ChatFormatting.RED + "创建configs文件夹失败");
                        break;
                    }
                }

                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(configDir);
                } catch (IOException e) {
                    NotifyUtil.notifyAsMessage(ChatFormatting.RED + "打开文件夹失败");
                }

                break;
        }
    }
}
