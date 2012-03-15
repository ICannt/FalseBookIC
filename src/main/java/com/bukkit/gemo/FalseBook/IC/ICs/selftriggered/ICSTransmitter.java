package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.event.block.SignChangeEvent;

public class ICSTransmitter extends SelftriggeredBaseIC {

    private String networkName = "";
    private String mainNetwork = "";
    boolean curStatus;

    public ICSTransmitter() {
        this.ICName = "TRANSMITTER";
        this.ICNumber = "ics.transmit";
        setICGroup(ICGroup.SELFTRIGGERED);
        this.chipState = new BaseChip(false, false, false, "Data", "", "");
        this.chipState.setOutputs("Output = Input", "", "");
        this.chipState.setLines("networkname", "");
        this.ICDescription = "The ics.transmit transmits the input value to a particular named band or network.";
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
    }

    public boolean onLoad(String[] lines) {
        this.networkName = "DEFAULT";
        if (lines[1].length() > 0) {
            this.networkName = lines[2];
        }
        if (lines[2].length() > 0) {
            this.mainNetwork = lines[3];
        }
        lines[1] = this.networkName;
        lines[2] = this.mainNetwork;
        return true;
    }

    public void setStatus(boolean newStatus) {
        if (!super.validateIC()) {
            return;
        }
        if (newStatus != this.oldStatus) {
            this.oldStatus = newStatus;

            for (Iterator<SelftriggeredBaseIC> iterator = this.core.getFactory().getSensorListIterator(); iterator.hasNext();) {
                SelftriggeredBaseIC IC = iterator.next();
                if ((!(IC instanceof ICSReceiver))
                        || (!this.networkName.equalsIgnoreCase(((ICSReceiver) IC).getNetworkName()))) {
                    continue;
                }
                if (!this.mainNetwork.equalsIgnoreCase(((ICSReceiver) IC).getMainNetwork())) {
                    continue;
                }
                ((ICSReceiver) IC).setStatus(newStatus);


            }

            switchLever(Lever.BACK, this.signBlock, newStatus);
        }
    }

    public boolean initIC(FalseBookICCore plugin, Location location) {
        boolean result = super.initIC(plugin, location);
        if (!result) {
            return false;
        }
        this.oldStatus = ((this.oldStatus) || (new InputState(this.signBlock).isInputOneHigh()));
        return result;
    }

    public boolean getStatus() {
        return this.oldStatus;
    }

    public String getNetworkName() {
        return this.networkName;
    }

    public String getMainNetwork() {
        return this.mainNetwork;
    }
}