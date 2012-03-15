package com.bukkit.gemo.FalseBook.IC.ICs;

import java.util.ArrayList;
import java.util.UUID;

public abstract class ExternalICPackage {

    private String API_VERSION = "UNKNOWN";
    private boolean showImportMessages = true;
    private ArrayList<Class<?>> ICList = new ArrayList<Class<?>>();

    public ArrayList<Class<?>> getICList() {
        return this.ICList;
    }

    public String getAPI_VERSION() {
        return this.API_VERSION;
    }

    public void setAPI_VERSION(String API_VERSION) {
        this.API_VERSION = API_VERSION;
    }

    protected void addIC(Class<?> clazz) {
        this.ICList.add(clazz);
    }
    
    protected void addUpgrader(String from, ICUpgrader upgrader) {
        ICUpgrade.addUpgrader(from, upgrader);
    }

    public boolean isShowImportMessages() {
        return this.showImportMessages;
    }

    protected boolean setShowImportMessages(boolean var) {
        this.showImportMessages = var;
        return this.showImportMessages;
    }
}