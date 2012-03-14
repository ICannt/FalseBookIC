package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.FalseBook.IC.WikiPage;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class cmdExportWiki extends Command {

    public cmdExportWiki(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Export the IC-Wiki";
    }

    public void execute(String[] args, CommandSender sender) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (!UtilPermissions.playerCanUseCommand(player, "falsebook.admin.ic")) {
                ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
                return;
            }
        }

        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Exporting Wiki");
        exportWiki();
        ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Done!");
    }

    public void exportWiki() {
        exportTICsToWiki();
        exportSTICsToWiki();

        exportWikiAllICs();
        exportWikiPackageICs("standard");
        exportWikiPackageICs("detection");
        exportWikiPackageICs("selftriggered");
        exportWikiPackageICs("worldedit");
        exportWikiPackageICs("datatype");
        exportWikiInformation();
    }

    public void exportSTICsToWiki() {
        ICFactory factory = FalseBookICCore.getInstance().getFactory();

        for (Iterator<SelftriggeredBaseIC> iterator = factory.getRegisteredSTICs().values().iterator(); iterator.hasNext();) {
            BaseIC IC = iterator.next();
            exportICAsWikiHTML(IC);
        }
    }

    public void exportTICsToWiki() {
        ICFactory factory = FalseBookICCore.getInstance().getFactory();

        for (Iterator<BaseIC> iterator = factory.getRegisteredTICs().values().iterator(); iterator.hasNext();) {
            BaseIC IC = iterator.next();
            exportICAsWikiHTML(IC);
        }
    }

    private void exportWikiPackageICs(String packageName) {
        ICFactory factory = FalseBookICCore.getInstance().getFactory();

        String folderName = "plugins/FalseBook/IC-Wiki";
        if (!new File(folderName + "/templates/template_packages.html").exists()) {
            return;
        }
        WikiPage thisPage = new WikiPage(folderName + "/templates/template_packages.html");
        String thisName = "";
        String icList = "";
        icList = icList + "<ul>";
        thisPage.replaceText("%PACKAGENAME%", packageName.toUpperCase() + " ICs");
        thisName = "";
        icList = "";
        icList = icList + "<ul>";

        TreeMap<String, BaseIC> newMapST = new TreeMap<String, BaseIC>();
        newMapST.putAll(factory.getRegisteredTICs());
        newMapST.putAll(factory.getRegisteredSTICs());
        icList = "<ul>";
        for (Iterator<BaseIC> iterator = newMapST.values().iterator(); iterator.hasNext();) {
            BaseIC IC = iterator.next();
            if (IC.getClass().getPackage().getName().contains(packageName)) {
                thisName = IC.getICNumber().substring(1, IC.getICNumber().length() - 1);
                icList = icList + "<li><a href=\"" + thisName + ".html\">" + thisName + "</a> - " + IC.getICName() + "</li>";
            }
        }
        icList = icList + "</ul>";
        thisPage.replaceText("%ICLIST%", icList);
        File newFile = new File(folderName + "/" + packageName + "_ics.html");
        if (newFile.exists()) {
            newFile.delete();
        }
        thisPage.savePage(folderName + "/" + packageName + "_ics.html");
        thisPage = null;
        newMapST.clear();
        newMapST = null;
    }

    private void exportWikiAllICs() {
        ICFactory factory = FalseBookICCore.getInstance().getFactory();

        String folderName = "plugins/FalseBook/IC-Wiki";
        if (!new File(folderName + "/templates/template_all.html").exists()) {
            return;
        }
        WikiPage thisPage = new WikiPage(folderName + "/templates/template_all.html");
        String thisName = "";
        String icList = "";
        icList = icList + "<ul>";

        TreeMap<String, BaseIC> newMap = new TreeMap<String, BaseIC>();
        newMap.putAll(factory.getRegisteredTICs());

        for (Iterator<BaseIC> iterator = newMap.values().iterator(); iterator.hasNext();) {
            BaseIC IC = iterator.next();
            thisName = IC.getICNumber().substring(1, IC.getICNumber().length() - 1);
            icList = icList + "<li><a href=\"" + thisName + ".html\">" + thisName + "</a> - " + IC.getICName() + "</li>";
        }
        icList = icList + "</ul>";
        thisPage.replaceText("%TICLIST%", icList);
        newMap.clear();
        newMap = null;

        TreeMap<String, BaseIC> newMapST = new TreeMap<String, BaseIC>();
        newMapST.putAll(factory.getRegisteredSTICs());
        icList = "<ul>";
        for (Iterator<BaseIC> iterator = newMapST.values().iterator(); iterator.hasNext();) {
            BaseIC IC = iterator.next();
            thisName = IC.getICNumber().substring(1, IC.getICNumber().length() - 1);
            icList = icList + "<li><a href=\"" + thisName + ".html\">" + thisName + "</a> - " + IC.getICName() + "</li>";
        }
        icList = icList + "</ul>";
        thisPage.replaceText("%STICLIST%", icList);
        thisPage.savePage(folderName + "/all_ics.html");
        thisPage = null;
        newMapST.clear();
        newMapST = null;
    }

    private void exportWikiInformation() {
        ICFactory factory = FalseBookICCore.getInstance().getFactory();

        String folderName = "plugins/FalseBook/IC-Wiki";
        if (!new File(folderName + "/templates/template_info.html").exists()) {
            return;
        }
        PluginDescriptionFile pdfFile = FalseBookICCore.getInstance().getDescription();
        String version = pdfFile.getVersion();

        WikiPage thisPage = new WikiPage(folderName + "/templates/template_info.html");
        thisPage.replaceText("%VERSION%", version);
        thisPage.replaceText("%TICCOUNT%", ((Integer) factory.getRegisteredTICsSize()).toString());
        thisPage.replaceText("%STICCOUNT%", ((Integer) factory.getRegisteredSTICsSize()).toString());

        thisPage.savePage(folderName + "/information.html");
        thisPage = null;
    }

    public void exportICAsWikiHTML(BaseIC ic) {
        String folderName = "plugins/FalseBook/IC-Wiki";
        File folder = new File(folderName);
        folder.mkdirs();

        File templateFile = new File(folderName + "/templates/template_icpage.html");
        if (!templateFile.exists()) {
            return;
        }
        try {
            String fileName = folderName + "/" + ic.ICNumber.substring(1, ic.ICNumber.length() - 1) + ".html";
            WikiPage thisPage = new WikiPage(folderName + "/templates/template_icpage.html");
            thisPage.replaceText("%MCNAME%", ic.ICNumber);
            thisPage.replaceText("%MCDESC%", ic.ICName);
            thisPage.replaceText("%DESCRIPTION%", ic.ICDescription);
            thisPage.replaceText("%IOIMAGE%", getIOLine(ic));
            thisPage.replaceText(" " + ic.ICNumber.substring(1, ic.ICNumber.length() - 1) + " ", " <b>" + ic.ICNumber.substring(1, ic.ICNumber.length() - 1) + "</b> ");
            thisPage.replaceText("%PERMISSIONS%", getPermissionString(ic));
            thisPage.replaceText("%SIGN%", getLineString(ic));
            thisPage.replaceText("%INPUTS%", getInputString(ic));
            thisPage.replaceText("%OUTPUTS%", getOutputString(ic));
            thisPage.savePage(fileName);
        } catch (Exception e) {
            FalseBookICCore.printInConsole("Error while exporting Wiki-File of " + ic.ICNumber);
        }
    }

    public String getInputString(BaseIC ic) {
        if ((ic.chipState.getNameInput1().length() == 0) && (ic.chipState.getNameInput2().length() == 0) && (ic.chipState.getNameInput3().length() == 0)) {
            return "no input";
        }

        String result = "";
        result = "<ol type=\"1\">";
        if (ic.chipState.getNameInput1().length() != 0) {
            result = result + "<li>" + ic.chipState.getNameInput1() + "</li>";
        }
        if (ic.chipState.getNameInput2().length() != 0) {
            result = result + "<li>" + ic.chipState.getNameInput2() + "</li>";
        }
        if (ic.chipState.getNameInput3().length() != 0) {
            result = result + "<li>" + ic.chipState.getNameInput3() + "</li>";
        }
        result = result + "</ol>";
        return result;
    }

    public String getOutputString(BaseIC ic) {
        if ((ic.chipState.getNameInput1().length() == 0) && (ic.chipState.getNameInput2().length() == 0) && (ic.chipState.getNameInput3().length() == 0)) {
            return "no output";
        }

        String result = "";
        result = "<ol type=\"1\">";
        if (ic.chipState.getNameInput1().length() != 0) {
            result = result + "<li>" + ic.chipState.getNameInput1() + "</li>";
        }
        if (ic.chipState.getNameInput2().length() != 0) {
            result = result + "<li>" + ic.chipState.getNameInput2() + "</li>";
        }
        if (ic.chipState.getNameInput3().length() != 0) {
            result = result + "<li>" + ic.chipState.getNameInput3() + "</li>";
        }
        result = result + "</ol>";
        return result;
    }

    public String getPermissionString(BaseIC ic) {
        String result = "";
        result = "<ul>";
        result = result + "<li>*.*</li>";
        result = result + "<li>falsebook.*</li>";
        result = result + "<li>falsebook.anyic</li>";
        result = result + "<li>falsebook.ic.*</li>";
        result = result + "<li>falsebook.ic." + ic.Group.name().toLowerCase() + "</li>";
        result = result + "<li>falsebook.ic." + ic.ICNumber.toLowerCase().substring(1, ic.ICNumber.length() - 1) + "</li>";
        result = result + "</ul>";
        return result;
    }

    public String getLineString(BaseIC ic) {
        if ((ic.chipState.getSignLine3().length() == 0) && (ic.chipState.getSignLine4().length() == 0)) {
            return "There are no sign parameters.";
        }

        String result = "<ul>";
        if (ic.chipState.getSignLine3().length() != 0) {
            result = result + "<li>Line 3: " + ic.chipState.getSignLine3() + "</li>";
        }
        if (ic.chipState.getSignLine4().length() != 0) {
            result = result + "<li>Line 4: " + ic.chipState.getSignLine4() + "</li>";
        }
        result = result + "</ul>";
        return result;
    }

    public String getIOLine(BaseIC ic) {
        return "<img class=\"floatRight\" src=\"images/IO/" + ic.chipState.getInputCount() + "I" + ic.chipState.getOutputCount() + "O.png\">";
    }
}