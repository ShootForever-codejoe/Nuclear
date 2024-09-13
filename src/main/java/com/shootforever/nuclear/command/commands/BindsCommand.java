package com.shootforever.nuclear.command.commands;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.command.Command;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.KeyboardUtil;
import com.shootforever.nuclear.util.NotifyUtil;
import net.minecraft.ChatFormatting;

public class BindsCommand extends Command {
    public BindsCommand() {
        super("binds");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 0) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "用法: .binds");
            return;
        }

        for (Module module : Nuclear.getInstance().getModuleManager().getModules()) {
            if (module.getKey() != 0) {
                NotifyUtil.notifyAsMessage(module.getName() + ": " + KeyboardUtil.getKeyName(module.getKey()) + "键");
            }
        }
    }
}
