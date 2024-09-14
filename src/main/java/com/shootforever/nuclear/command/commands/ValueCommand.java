package com.shootforever.nuclear.command.commands;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.command.Command;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.NotifyUtil;
import com.shootforever.nuclear.value.SingleValue;
import com.shootforever.nuclear.value.Value;
import net.minecraft.ChatFormatting;

import java.util.List;

public class ValueCommand extends Command {
    public ValueCommand() {
        super("value");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 3) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "用法: .value <module> <value_name> <value>");
            return;
        }

        Module module = Nuclear.getInstance().getModuleManager().getModule(args[0]);
        if (module == null) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "模块" + args[0] + "不存在");
            return;
        }

        List<SingleValue> singleValues = Value.getSingleValues(module.getValues());
        for (SingleValue singleValue : singleValues) {
            if (singleValue.getName().equalsIgnoreCase(args[1])) {

            }
        }
    }
}
