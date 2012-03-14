package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdSetTicksBetween extends Command {

    public cmdSetTicksBetween(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Set the ticks between the execution of selftriggered ICs";
    }

    public void execute(String[] args, CommandSender sender) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (!UtilPermissions.playerCanUseCommand(player, "falsebook.admin.ic")) {
                ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
                return;
            }

        }

        if (!Parser.isInteger(args[0])) {
            ChatUtils.printError(sender, this.pluginName, "The argument must be an integer.");
            return;
        }

        FalseBookICCore core = FalseBookICCore.getInstance();
        int ticks = Parser.getInteger(args[0], 0);
        core.saveSettings("FalseBook/FalseBookIC.properties");
        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "TicksBetween execution set to: " + ticks);
        ChatUtils.printLine(sender, ChatColor.GRAY, "Type /reload to apply the changes.");
    }
}