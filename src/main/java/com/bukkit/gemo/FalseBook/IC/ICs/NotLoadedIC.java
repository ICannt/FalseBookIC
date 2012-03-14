package com.bukkit.gemo.FalseBook.IC.ICs;

import org.bukkit.Location;

public class NotLoadedIC {

    private int ID = -1;
    private String ICNumber = "";
    private String ICName = "";
    private Location ICLocation = null;

    public NotLoadedIC(int ID, Location location) {
        this.ID = ID;
        this.ICLocation = location;
    }

    public NotLoadedIC(int ID, String ICNumber, Location location) {
        this(ID, location);
        this.ICNumber = ICNumber;
    }

    public NotLoadedIC(int ID, String ICNumber, String ICName, Location location) {
        this(ID, ICNumber, location);
        this.ICName = ICName;
    }

    public int getID() {
        return this.ID;
    }

    public String getICNumber() {
        return this.ICNumber;
    }

    public String getName() {
        return this.ICName;
    }

    public Location getICLocation() {
        return this.ICLocation;
    }
}