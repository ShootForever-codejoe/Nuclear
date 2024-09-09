package com.shootforever.nuclear.command;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.command.commands.*;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.ChatEvent;
import com.shootforever.nuclear.util.NotifyUtil;
import net.minecraft.ChatFormatting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandManager() {
        Nuclear.getInstance().getEventManager().register(this);

        registerCommand(new BindCommand());
        registerCommand(new EnabledCommand());
        registerCommand(new BindsCommand());
    }

    private void registerCommand(Command command) {
        commandMap.put(command.getName().toLowerCase(), command);
    }

    @EventTarget
    public void onChat(ChatEvent event) {
        if (!event.getMessage().startsWith(".")) return;

        String[] args = event.getMessage().substring(1).split(" ");
        String name = args[0];
        Command command = commandMap.get(name.toLowerCase());
        if (command == null) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "命令" + name + "不存在");
        } else {
            command.execute(Arrays.copyOfRange(args, 1, args.length));
        }
        event.setCancelled(true);
    }

    public Command getCommand(String name) {
        return commandMap.get(name.toLowerCase());
    }
}
