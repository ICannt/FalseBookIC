package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.ICSTransmitter;
import com.bukkit.gemo.utils.SignUtils;
import java.util.Iterator;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class ICReceiver extends BaseIC {

    public ICReceiver() {
        this.ICName = "RECEIVER";
        this.ICNumber = "ic.receive";
        setICGroup(ICGroup.STANDARD);
        this.chipState = new BaseChip(true, false, false, "Clock", "", "");
        this.chipState.setOutputs("Received data", "", "");
        this.chipState.setLines("networkname", "");
        this.ICDescription = "The ic.receive receives the state in a particular band or network when the clock input goes from low to high.<br /><br />The corresponding transmitter is the ics.transmit.";
    }

    public void checkCreation(SignChangeEvent event) {
        if (event.getLine(1).length() < 1) {
            SignUtils.cancelSignCreation(event, "Please define a Networkname!");
            return;
        }
    }

    public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
        if ((currentInputs.isInputOneHigh()) && (previousInputs.isInputOneLow())) {
            String networkName = signBlock.getLine(1);
            String mainNetwork = signBlock.getLine(2);
            if (networkName.length() > 0) {
                boolean result = false;

                for (Iterator<SelftriggeredBaseIC> iterator = this.core.getFactory().getSensorListIterator(); iterator.hasNext();) {
                    SelftriggeredBaseIC IC = iterator.next();
                    if ((!(IC instanceof ICSTransmitter))
                            || (!networkName.equalsIgnoreCase(((ICSTransmitter) IC).getNetworkName()))
                            || (!((ICSTransmitter) IC).getStatus())) {
                        continue;
                    }
                    if (!mainNetwork.equalsIgnoreCase(((ICSTransmitter) IC).getMainNetwork())) {
                        continue;
                    }
                    result = true;
                    break;

                }

                switchLever(Lever.BACK, signBlock, result);
            }
        }
    }
}