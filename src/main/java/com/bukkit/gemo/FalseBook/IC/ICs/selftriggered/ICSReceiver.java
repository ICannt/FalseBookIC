package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.event.block.SignChangeEvent;

public class ICSReceiver extends SelftriggeredBaseIC {

    private String networkName = "";
    private String mainNetwork = "";

    public String getNetworkName() {
        return this.networkName;
    }

    public ICSReceiver() {
        this.ICName = "RECEIVER";
        this.ICNumber = "ics.receive";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "", "", "");
        this.chipState.setOutputs("Received data", "", "");
        this.chipState.setLines("networkname", "");
        this.ICDescription = "The ics.receive receives the state in a particular band or network.<br /><br />The corresponding transmitter is the ics.transmit.";
    }

    public void checkCreation(SignChangeEvent event) {
        if (event.getLine(1) == null) {
            SignUtils.cancelSignCreation(event, "Please define a Networkname!");
            return;
        }
        if (event.getLine(1).length() < 1) {
            SignUtils.cancelSignCreation(event, "Please define a Networkname!");
            return;
        }
        this.networkName = event.getLine(1);

        if (event.getLine(2).length() > 0) {
            this.mainNetwork = event.getLine(2);
        }
    }

    public boolean onLoad(String[] lines) {
        this.networkName = "DEFAULT";
        if (lines[1].length() > 0) {
            this.networkName = lines[1];
        } else {
            lines[1] = this.networkName;
        }
        if (lines[2].length() > 0) {
            this.mainNetwork = lines[2];
        } else {
            lines[2] = this.mainNetwork;
        }
        return true;
    }

    public void setStatus(boolean newStatus) {
        if (newStatus != this.oldStatus) {
            this.oldStatus = newStatus;
            switchLever(Lever.BACK, this.signBlock, newStatus);
        }
    }

    public String getMainNetwork() {
        return this.mainNetwork;
    }
}