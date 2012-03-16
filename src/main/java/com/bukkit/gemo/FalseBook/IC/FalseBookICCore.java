package com.bukkit.gemo.FalseBook.IC;

import com.bukkit.gemo.FalseBook.Core.FalseBookCore;
import com.bukkit.gemo.FalseBook.IC.Listeners.FalseBookICBlockListener;
import com.bukkit.gemo.FalseBook.IC.Listeners.FalseBookICEntityListener;
import com.bukkit.gemo.FalseBook.IC.Listeners.FalseBookICPlayerListener;
import com.bukkit.gemo.FalseBook.IC.commands.*;
import com.bukkit.gemo.commands.CommandList;
import com.bukkit.gemo.utils.FlatFile;
import com.grover.mingebag.ic.RedstoneListener;

import java.io.File;
import java.io.IOException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FalseBookICCore extends JavaPlugin
        implements Runnable {

    private static FalseBookICCore instance;
    private FlatFile config;
    private String pluginName;
    private int ticks = 10;
    private int timeBetweenTicksInMS = 500;
    private long lastExecutionEvent = 0L;
    private boolean loadUnloadedChunks = true;
    private int maxReplaceBlocks = 2500;
    private int mainTaskID = -1;
    private boolean useMySQL = false;
    private boolean allowExplosionForICs = false;
    private FalseBookCore core;
    private FalseBookICBlockListener blockListener;
    private FalseBookICEntityListener entityListener;
    private FalseBookICPlayerListener playerListener;
    private boolean enableSTICs = true;
    private CommandList commandList;
    private ICFactory factory = null;
    private PersistenceHandler persistenceHandler = null;

    public PersistenceHandler getPersistenceHandler() {
        return this.persistenceHandler;
    }

    public static void printInConsole(String str) {
        System.out.println("[ FalseBook IC ] " + str);
    }

    public void run() {
        if (System.currentTimeMillis() >= this.lastExecutionEvent + this.timeBetweenTicksInMS) {
            this.lastExecutionEvent = System.currentTimeMillis();
            this.factory.executeSTICs();
        }
    }

    public void onDisable() {
        if (searchCore()) {
            this.persistenceHandler.closeSQL();
            printInConsole(this.pluginName + " disabled");
        }
    }

    public void onEnable() {
        instance = this;
        if (!searchCore()) {
            printInConsole(">>>>> FalseBookCore.jar not found! FalseBookIC.jar is being disabled!");
            return;
        }

        initCommands();

        this.blockListener = new FalseBookICBlockListener();
        this.entityListener = new FalseBookICEntityListener();
        this.playerListener = new FalseBookICPlayerListener();




        this.factory = new ICFactory(this);
        this.persistenceHandler = new PersistenceHandler(this);

        this.blockListener.init(this.factory);
        this.entityListener.init(this.factory);
        this.playerListener.init(this.factory);
        this.factory.init(this.persistenceHandler);
        this.persistenceHandler.init(this.factory);

        loadVersion();
        loadSettings("FalseBook/FalseBookIC.properties");

        registerListeners();

        if (!this.useMySQL) {
            this.persistenceHandler.initSQLite();
        } else {
            this.persistenceHandler.initMySQL();
        }
        this.factory.importSelfmadeICs();

        if (this.enableSTICs) {
            getServer().getScheduler().scheduleSyncDelayedTask(this, new DelayedDataLoader(this.persistenceHandler), 20L);
            this.mainTaskID = getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 1L, 1L);
        }

        printInConsole(this.pluginName + " enabled");

        // Grover data type system
        getServer().getPluginManager().registerEvents(new RedstoneListener(factory), this);

    }

    private void initCommands() {
        com.bukkit.gemo.commands.Command[] commands = {
            new cmdFBIC("[FB-IC]", "/fbic", "", "", false, new com.bukkit.gemo.commands.Command[]{
                new cmdClearFailedICs("[FB-IC]", "clearallfailed", "", ""),
                new cmdClearFailedICs("[FB-IC]", "deleteallfailed", "", ""),
                new cmdClearFailedICs("[FB-IC]", "delallfailed", "", ""),
                new cmdClearFailedICs("[FB-IC]", "clearfailedics", "", ""),
                new cmdClearFailedICs("[FB-IC]", "deletefailedics", "", ""),
                new cmdClearFailedICs("[FB-IC]", "delfailedics", "", ""),
                new cmdClearICs("[FB-IC]", "clearics", "", ""),
                new cmdJumpToFailed("[FB-IC]", "jump", "<ID>", ""),
                new cmdJumpToFailed("[FB-IC]", "jumpto", "<ID>", ""),
                new cmdDeleteFailedIC("[FB-IC]", "deleteic", "<ID>", ""),
                new cmdDeleteFailedIC("[FB-IC]", "delic", "<ID>", ""),
                new cmdExportWiki("[FB-IC]", "exportwiki", "", ""),
                new cmdExportJSON("[FB-IC]", "exportjson", "", ""),
                new cmdICStatus("[FB-IC]", "icstatus", "", ""),
                new cmdReloadICs("[FB-IC]", "reloadics", "", ""),
                new cmdReloadSettings("[FB-IC]", "reloadsettings", "", ""),
                new cmdSetCommand("[FB-IC]", "set", "", "", false, new com.bukkit.gemo.commands.Command[]{
                    new cmdSetMaxReplaceBlocks("[FB-IC]", "maxreplaceblocks", "<Number of Blocks>", ""),
                    new cmdSetSelftriggeredICs("[FB-IC]", "selftriggeredics", "<true | false>", ""),
                    new cmdSetTicksBetween("[FB-IC]", "ticksbetween", "<Number of ticks>", "")})})};

        this.commandList = new CommandList("[FB-Chat]", commands);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(this.blockListener, this);

        getServer().getPluginManager().registerEvents(this.playerListener, this);

        getServer().getPluginManager().registerEvents(this.entityListener, this);
    }

    public boolean loadSettings(String FileName) {
        try {
            this.config = new FlatFile(FileName, false);
            if (this.config.readFile()) {
                this.loadUnloadedChunks = this.config.getBoolean("LoadUnloadedChunks", this.loadUnloadedChunks);
                this.enableSTICs = this.config.getBoolean("SelftriggeredICs", this.enableSTICs);
                this.ticks = this.config.getInt("TicksBetween", this.ticks);
                this.maxReplaceBlocks = this.config.getInt("maxReplaceBlocks", this.maxReplaceBlocks);
                this.allowExplosionForICs = this.config.getBoolean("allowExplosionForICs", this.allowExplosionForICs);
                this.useMySQL = this.config.getBoolean("useMySQL", this.useMySQL);

                saveSettings(FileName);
            } else {
                saveSettings(FileName);
            }

            this.timeBetweenTicksInMS = (int) (1000.0D * (this.ticks / 20.0D));
            printInConsole("Selftriggered ICs are executing aprox. every " + this.timeBetweenTicksInMS + "ms.");
            this.lastExecutionEvent = System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            try {
                this.config.regenerateFile(FileName);
                saveSettings(FileName);

                this.timeBetweenTicksInMS = (int) (1000.0D * (this.ticks / 20.0D));
                printInConsole("Selftriggered ICs are executing aprox. every " + this.timeBetweenTicksInMS + "ms.");
                this.lastExecutionEvent = System.currentTimeMillis();
            } catch (IOException localIOException) {
            }
            printInConsole("Error while reading file: plugins/" + FileName);
        }
        return false;
    }

    public boolean saveSettings(String FileName) {
        File folder = new File("plugins/FalseBook");
        folder.mkdirs();
        try {
            this.config = new FlatFile(FileName, false);

            this.config.setBoolean("SelftriggeredICs", this.enableSTICs);
            this.config.setInt("TicksBetween", this.ticks);
            this.config.setInt("maxReplaceBlocks", this.maxReplaceBlocks);
            this.config.setBoolean("LoadUnloadedChunks", this.loadUnloadedChunks);
            this.config.setBoolean("allowExplosionForICs", this.allowExplosionForICs);
            this.config.setBoolean("useMySQL", this.useMySQL);
            this.config.writeFile();
            return true;
        } catch (IOException e) {
            printInConsole("Error while saving file: plugins/" + FileName);
            e.printStackTrace();
        }
        return false;
    }

    private void loadVersion() {
        PluginDescriptionFile pdfFile = getDescription();
        this.pluginName = (pdfFile.getName() + " v" + pdfFile.getVersion() + " by GeMo");
    }

    private boolean searchCore() {
        PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("FalseBookCore") != null) {
            this.core = ((FalseBookCore) pm.getPlugin("FalseBookCore"));
            PluginDescriptionFile thisPDFFile = getDescription();
            PluginDescriptionFile corePDFFile = this.core.getDescription();
            if (!thisPDFFile.getVersion().equalsIgnoreCase(corePDFFile.getVersion())) {
                printInConsole("WRONG CORE-VERSION!");
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (searchCore()) {
            this.commandList.handleCommand(sender, label, args);
        }
        return true;
    }

    public String getPluginName() {
        return this.pluginName;
    }

    public ICFactory getFactory() {
        return this.factory;
    }

    public boolean isLoadUnloadedChunks() {
        return this.loadUnloadedChunks;
    }

    public int getMaxReplaceBlocks() {
        return this.maxReplaceBlocks;
    }

    public void setMaxReplaceBlocks(int maxReplaceBlocks) {
        this.maxReplaceBlocks = maxReplaceBlocks;
    }

    public boolean isEnableSTICs() {
        return this.enableSTICs;
    }

    public void setEnableSTICs(boolean enableSTICs) {
        this.enableSTICs = enableSTICs;
    }

    public static FalseBookICCore getInstance() {
        return instance;
    }

    public int getMainTaskID() {
        return this.mainTaskID;
    }

    public void setMainTaskID(int mainTaskID) {
        this.mainTaskID = mainTaskID;
    }

    public boolean isAllowExplosionForICs() {
        return this.allowExplosionForICs;
    }

    public boolean isUseMySQL() {
        return this.useMySQL;
    }
}