package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.commands.SuperCommand;
import com.bukkit.gemo.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class cmdSetCommand extends SuperCommand {

    public cmdSetCommand(String pluginName, String syntax, String arguments, String node, boolean hasFunction, Command[] subCommands) {
        super(pluginName, syntax, arguments, node, hasFunction, subCommands);
        this.description = "Change some settings";
    }

    public void execute(String[] args, CommandSender sender) {
        ChatUtils.printLine(sender, ChatColor.AQUA, "-------------- [ FalseBookIC Help ] --------------");
        Command[] commands = getSubCommands();
        for (Command command : commands) {
            ChatUtils.printLine(sender, ChatColor.GRAY, command.getHelpMessage());
        }
    }

    public void run(String[] args, CommandSender sender) {
        if (!runSubCommand(args, sender)) {
            super.run(args, sender);
        }
    }
}