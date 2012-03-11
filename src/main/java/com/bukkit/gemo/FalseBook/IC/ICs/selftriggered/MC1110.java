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

public class MC1110 extends SelftriggeredBaseIC
{
  private String networkName = "";
  private String mainNetwork = "";
  boolean curStatus;

  public MC1110()
  {
    setTypeID(2);
    this.ICName = "TRANSMITTER";
    this.ICNumber = "[MC1110]";
    setICGroup(ICGroup.SELFTRIGGERED);
    this.chipState = new BaseChip(false, false, false, "Data", "", "");
    this.chipState.setOutputs("Output = Input", "", "");
    this.chipState.setLines("networkname", "");
    this.ICDescription = "The MC1110 transmits the input value to a particular named band or network.";
  }

  public void checkCreation(SignChangeEvent event)
  {
    if (event.getLine(2) == null) {
      SignUtils.cancelSignCreation(event, "Please define a Networkname!");
      return;
    }
    if (event.getLine(2).length() < 1) {
      SignUtils.cancelSignCreation(event, "Please define a Networkname!");
      return;
    }
  }

  public boolean onLoad(String[] lines)
  {
    this.networkName = "DEFAULT";
    if (lines[2].length() > 0) {
      this.networkName = lines[2];
    }
    if (lines[3].length() > 0) {
      this.mainNetwork = lines[3];
    }
    lines[2] = this.networkName;
    lines[3] = this.mainNetwork;
    return true;
  }

  public void setStatus(boolean newStatus) {
    if (!super.validateIC()) {
      return;
    }
    if (newStatus != this.oldStatus) {
      this.oldStatus = newStatus;

      for (Iterator<SelftriggeredBaseIC> iterator = this.core.getFactory().getSensorListIterator(); iterator.hasNext(); ) {
        SelftriggeredBaseIC IC = iterator.next();
        if ((!(IC instanceof MC0111)) || 
          (!this.networkName.equalsIgnoreCase(((MC0111)IC).getNetworkName())))
          continue;
        if (!this.mainNetwork.equalsIgnoreCase(((MC0111)IC).getMainNetwork())) {
          continue;
        }
        ((MC0111)IC).setStatus(newStatus);
        
      }

      switchLever(Lever.BACK, this.signBlock, newStatus);
    }
  }

  public boolean initIC(FalseBookICCore plugin, Location location)
  {
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