package com.shootforever.nuclear.command.commands;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.command.Command;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.KeyboardUtil;
import com.shootforever.nuclear.util.NotifyUtil;
import net.minecraft.ChatFormatting;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind");
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

        Integer key = KeyboardUtil.getKeyNumber(args[1]);
        module.setKey(key);
        NotifyUtil.notifyAsMessage("模块" + module.getName() + "成功绑定为" + KeyboardUtil.getKeyName(key) + "键");
    }


}
