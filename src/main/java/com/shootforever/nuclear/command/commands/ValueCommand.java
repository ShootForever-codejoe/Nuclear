package com.shootforever.nuclear.command.commands;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.command.Command;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.NotifyUtil;
import com.shootforever.nuclear.value.Value;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.ChoiceValue;
import com.shootforever.nuclear.value.values.NumberValue;
import net.minecraft.ChatFormatting;

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

        Value<?> value = module.getValue(args[1]);
        if (value == null) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "模块" + module.getName() + "的值" + args[1] + "不存在");
            return;
        }

        if (value instanceof NumberValue numberValue) {
            float number;
            try {
                number = Float.parseFloat(args[2]);
            } catch (NumberFormatException e) {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "模块" + module.getName() + "的值" + value.getName() + "只能是数字");
                return;
            }
            if ((number / numberValue.getStep()) % 1 == 0 /* 判断number/step是否为整数 */ ) {
                numberValue.setValue(number);
            } else {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "模块" + module.getName() + "的值" + value.getName() + "的最小间隔为" + numberValue.getStep());
            }
        } else if (value instanceof BooleanValue booleanValue) {
            if (args[2].equalsIgnoreCase("on")) {
                booleanValue.setValue(true);
            } else if (args[2].equalsIgnoreCase("off")) {
                booleanValue.setValue(false);
            } else {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "模块" + module.getName() + "的值" + value.getName() + "只能为on或off");
            }
        } else if (value instanceof ChoiceValue choiceValue) {
            if (choiceValue.isChoice(args[2])) {
                choiceValue.setValue(args[2]);
            } else {
                NotifyUtil.notifyAsMessage(ChatFormatting.RED + "模块" + module.getName() + "的值" + value.getName() + "不存在选项" + args[2]);
            }
        }
    }
}
