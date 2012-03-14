package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.DelayedDataLoader;
import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdReloadICs extends Command {

    public cmdReloadICs(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Reload the ICs";
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

        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Reloading ICs...");
        if (core.getMainTaskID() != -1) {
            Bukkit.getServer().getScheduler().cancelTask(core.getMainTaskID());
        }
        factory.clearFailedICs();
        factory.clearSensorList();
        DelayedDataLoader loader = new DelayedDataLoader(core.getPersistenceHandler());
        loader.run();

        if (core.isEnableSTICs()) {
            core.setMainTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(core, core, 1L, 1L));
        }
        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, factory.getSensorListSize() + " selftriggered ICs loaded.");
    }
}