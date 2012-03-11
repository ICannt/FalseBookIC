package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdClearICs extends Command
{
  public cmdClearICs(String pluginName, String syntax, String arguments, String node)
  {
    super(pluginName, syntax, arguments, node);
    this.description = "Clear ALL ICs";
  }

  public void execute(String[] args, CommandSender sender)
  {
    if ((sender instanceof Player)) {
      Player player = (Player)sender;
      if (!UtilPermissions.playerCanUseCommand(player, "falsebook.admin.ic")) {
        ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
        return;
      }

    }

    FalseBookICCore core = FalseBookICCore.getInstance();
    ICFactory factory = core.getFactory();

    ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Clearing ICs...");
    if (core.getMainTaskID() != -1) {
      Bukkit.getServer().getScheduler().cancelTask(core.getMainTaskID());
    }
    factory.clearSensorList();
    core.getPersistenceHandler().clearAllSelftriggeredICs();
    ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Selftriggered ICs cleared successfully!");
  }
}