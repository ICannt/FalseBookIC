package com.bukkit.gemo.FalseBook.IC.ICs;

import org.bukkit.block.Sign;

public interface ICUpgrader {
    public boolean preCheckUpgrade(Sign icSign);
    
    public void upgrade(Sign icSign);
}
