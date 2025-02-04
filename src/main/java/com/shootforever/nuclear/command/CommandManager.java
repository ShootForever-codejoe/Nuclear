package com.shootforever.nuclear.command;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.command.commands.*;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.ChatEvent;
import com.shootforever.nuclear.util.NotifyUtil;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        Nuclear.getInstance().getEventManager().register(this);

        registerCommands(
                new BindCommand(),
                new EnabledCommand(),
                new BindsCommand(),
                new ValueCommand(),
                new ConfigCommand()
        );
    }

    private void registerCommands(@NotNull Command @NotNull ... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    private void registerCommand(@NotNull Command command) {
        commands.add(command);
    }

    @EventTarget
    public void onChat(ChatEvent event) {
        if (!event.getMessage().startsWith(".")) return;

        String[] args = event.getMessage().substring(1).split(" ");
        String name = args[0];
        Command command = getCommand(name);
        if (command == null) {
            NotifyUtil.notifyAsMessage(ChatFormatting.RED + "命令" + name + "不存在");
        } else {
            command.execute(Arrays.copyOfRange(args, 1, args.length));
        }
        event.setCancelled(true);
    }

    public @Nullable Command getCommand(@NotNull String name) {
        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    public List<Command> getCommands() {
        return new ArrayList<>(commands);
    }
}
