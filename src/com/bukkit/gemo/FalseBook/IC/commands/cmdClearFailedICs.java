package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.NotLoadedIC;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdClearFailedICs extends Command
{
  public cmdClearFailedICs(String pluginName, String syntax, String arguments, String node)
  {
    super(pluginName, syntax, arguments, node);
    this.description = "Clear all failed ICs";
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

    ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Clearing " + ChatColor.RED + factory.getFailedICsSize() + ChatColor.YELLOW + "  failed ICs...");
    for (NotLoadedIC thisIC : factory.getFailedICs()) {
      core.getPersistenceHandler().removeSelftriggeredIC(thisIC.getID());
      factory.removeFailedIC(thisIC);
    }
    ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Failed ICs cleared successfully!");
  }
}