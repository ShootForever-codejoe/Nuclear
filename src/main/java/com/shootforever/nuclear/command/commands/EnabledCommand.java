package com.shootforever.nuclear.command.commands;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.command.Command;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.NotifyUtil;
import net.minecraft.ChatFormatting;

public class EnabledCommand extends Command {
    public EnabledCommand() {
        super("enabled");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1 && args.length != 2) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "用法: .enable <module> [<enabled(on/off)>]");
            return;
        }

        Module module = Nuclear.getInstance().getModuleManager().getModule(args[0]);
        if (module == null) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "模块" + args[0] + "不存在");
            return;
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("on")) {
                module.setEnabled(true);
            } else if (args[1].equalsIgnoreCase("off")) {
                module.setEnabled(false);
            } else {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "第二个参数enabled只能为on或off");
            }
        } else {
            module.setEnabled(!module.isEnabled());
        }
    }
}
