package com.bukkit.gemo.FalseBook.IC.ICs;

import java.util.HashMap;

public final class ICUpgrade {
    private static HashMap<String, ICUpgrader> upgrades = new HashMap<String, ICUpgrader>();
    
    public static void addUpgrader(String from, ICUpgrader upgrader) {
        from = from.toLowerCase();
        upgrades.put(from, upgrader);
    }
    
    public static boolean needsUpgrade(String icName) {
        icName = icName.toLowerCase();
        
        return upgrades.containsKey(icName);
    }
    
    public static ICUpgrader getUpgrader(String icName) {
        icName = icName.toLowerCase();
        
        return upgrades.get(icName);
    }
}
