package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.NotLoadedIC;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdDeleteFailedIC extends Command {

    public cmdDeleteFailedIC(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Delete a failed IC";
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
        ICFactory factory = core.getFactory();
        int failedID = Parser.getInteger(args[0], 0);

        boolean found = false;
        for (NotLoadedIC thisIC : factory.getFailedICs()) {
            if (thisIC.getID() == failedID) {
                ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Deleting failed IC " + thisIC.getICNumber() + " with ID: " + thisIC.getID());
                core.getPersistenceHandler().removeSelftriggeredIC(thisIC.getID());
                factory.removeFailedIC(thisIC);
                found = true;
                break;
            }
        }
        if (!found) {
            ChatUtils.printError(sender, "[FB-IC]", "The given ID was not found!");
        } else {
            ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Done!");
        }
    }
}