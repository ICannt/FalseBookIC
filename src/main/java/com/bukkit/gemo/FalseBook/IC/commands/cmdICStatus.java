package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.NotLoadedIC;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.text.DecimalFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdICStatus extends Command {

    public cmdICStatus(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Show status of ICs";
    }

    public void execute(String[] args, CommandSender sender) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (!UtilPermissions.playerCanUseCommand(player, "falsebook.admin.ic")) {
                ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
                return;
            }

        }

        FalseBookICCore core = FalseBookICCore.getInstance();
        ICFactory factory = core.getFactory();
        ChatUtils.printLine(sender, ChatColor.AQUA, "[FalseBook IC Status]:");
        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Selftriggered ICs loaded: " + ChatColor.GREEN + factory.getSensorListSize());
        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Selftriggered ICs NOT loaded: " + ChatColor.RED + factory.getFailedICsSize());
        if (factory.getFailedICsSize() > 0) {
            ChatUtils.printLine(sender, ChatColor.AQUA, "List of failed ICs: ");
            for (NotLoadedIC thisIC : factory.getFailedICs()) {
                ChatUtils.printError(sender, "[FB-IC]", "ID: " + thisIC.getID() + ", " + thisIC.getICNumber() + " @ World: " + thisIC.getICLocation().getWorld().getName() + " , X: " + thisIC.getICLocation().getBlockX() + " , Y: " + thisIC.getICLocation().getBlockY() + " , Z: " + thisIC.getICLocation().getBlockZ());
            }
        }

        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Selftriggered ICs are enabled: " + core.isEnableSTICs());

        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Load unloaded chunks: " + core.isLoadUnloadedChunks());

        ChatUtils.printInfo(sender, "", ChatColor.WHITE, " ");
        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Execution of selftriggered ICs took:");

        DecimalFormat formater = new DecimalFormat("#.###");
        ChatUtils.printInfo(sender, core.getPluginName(), ChatColor.AQUA, "executioncount - min. time in ms / max. time in ms / average time ms");
        ChatUtils.printInfo(sender, core.getPluginName(), ChatColor.GRAY, "-----------------------------------------------");
        ChatUtils.printInfo(sender, core.getPluginName(), ChatColor.GRAY, factory.statistic.eventCount + " - " + formater.format(factory.statistic.minTime / 1000000.0D) + "ms / " + formater.format(factory.statistic.maxTime / 1000000.0D) + "ms / " + formater.format(factory.statistic.allTime / factory.statistic.eventCount / 1000000.0D) + "ms");
    }
}