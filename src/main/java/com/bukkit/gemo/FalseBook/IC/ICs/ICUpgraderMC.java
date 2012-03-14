package com.bukkit.gemo.FalseBook.IC.ICs;

import org.bukkit.block.Sign;

public class ICUpgraderMC implements ICUpgrader{
    private String destName;
    
    public ICUpgraderMC(String newName) {
        this.destName = newName;
    }

    @Override
    public boolean preCheckUpgrade(Sign icSign) {
        return true;
    }

    @Override
    public void upgrade(Sign icSign) {
        icSign.setLine(0, this.destName);
        icSign.setLine(1, icSign.getLine(2));
        icSign.setLine(2, icSign.getLine(3));
        icSign.setLine(3, "");
        icSign.update(true);
    }
}
