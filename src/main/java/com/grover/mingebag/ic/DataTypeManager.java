package com.grover.mingebag.ic;

import java.util.HashMap;

import org.bukkit.Location;

public class DataTypeManager {

    private HashMap<Location, DataType> typemap = new HashMap<Location, DataType>();

    public void addDataType(Location location, DataType type) {
        typemap.put(location, type);
    }

    public DataType getDataType(Location location) {
        if (typemap.containsKey(location)) {
            return typemap.get(location);
        }
        return null;
    }

    public boolean hasDataType(Location location) {
        return typemap.containsKey(location);
    }

    public void carryDataType(Location current_location, Location new_location) {
        if (typemap.containsKey(current_location)) {
            DataType original = typemap.get(current_location);
            addDataType(new_location, original);
            return;
        }
    }

    public void moveDataType(Location current_location, Location new_location) {
        if (typemap.containsKey(current_location)) {
            DataType old = typemap.get(current_location);
            typemap.remove(current_location);
            addDataType(new_location, old);
            return;
        }
    }

    public void endDataType(Location location) {
        if (typemap.containsKey(location)) {
            typemap.remove(location);
        }
    }

    public void clean() {
        for (Location entry : typemap.keySet()) {
            if (!entry.getChunk().isLoaded()) {
                endDataType(entry);
            }
        }
    }
}
