package com.grover.mingebag.ic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.bukkit.gemo.FalseBook.IC.ICFactory;

public class RedstoneListener implements Listener {

    ICFactory factory;

    public RedstoneListener(ICFactory factory) {
        this.factory = factory;
    }

    @EventHandler
    public void remove(BlockBreakEvent event) {
        if (factory.getDataTypeManager().hasDataType(event.getBlock().getLocation())) {
            factory.getDataTypeManager().endDataType(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void handleData(BlockRedstoneEvent event) {
        // Remove old datatypes
        if (event.getNewCurrent() == 0 && event.getBlock().getType() == Material.REDSTONE_WIRE) {
            if (factory.getDataTypeManager().hasDataType(event.getBlock().getLocation())) {
                factory.getDataTypeManager().endDataType(event.getBlock().getLocation());
                return;
            }
        }

        // check around for datatypes and move them on
        if (event.getBlock().getType() == Material.REDSTONE_WIRE && event.getNewCurrent() > 0) {
            // There can only be one datatype carried on; so the first found takes priority. People generally shouldnt mix datatypes anyway
            Location loc;

            // flat x/z
            loc = event.getBlock().getLocation();
            loc.setZ(loc.getZ() + 1);
            if (factory.getDataTypeManager().hasDataType(loc)) {
                factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                return;
            }

            loc = event.getBlock().getLocation();
            loc.setZ(loc.getZ() - 1);
            if (factory.getDataTypeManager().hasDataType(loc)) {
                factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                return;
            }

            loc = event.getBlock().getLocation();
            loc.setX(loc.getX() + 1);
            if (factory.getDataTypeManager().hasDataType(loc)) {
                factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                return;
            }

            loc = event.getBlock().getLocation();
            loc.setX(loc.getX() - 1);
            if (factory.getDataTypeManager().hasDataType(loc)) {
                factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                return;
            }

            // New block is lower than old block
            loc = event.getBlock().getLocation();
            loc.setY(loc.getY() + 1);
            if (event.getBlock().getWorld().getBlockAt(loc).getType() == Material.AIR) {
                loc = event.getBlock().getLocation();
                loc.setY(loc.getY() + 1);
                loc.setZ(loc.getZ() + 1);
                if (factory.getDataTypeManager().hasDataType(loc)) {
                    factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                    return;
                }

                loc = event.getBlock().getLocation();
                loc.setY(loc.getY() + 1);
                loc.setZ(loc.getZ() - 1);
                if (factory.getDataTypeManager().hasDataType(loc)) {
                    factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                    return;
                }

                loc = event.getBlock().getLocation();
                loc.setY(loc.getY() + 1);
                loc.setX(loc.getX() + 1);
                if (factory.getDataTypeManager().hasDataType(loc)) {
                    factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                    return;
                }

                loc = event.getBlock().getLocation();
                loc.setY(loc.getY() + 1);
                loc.setX(loc.getX() - 1);
                if (factory.getDataTypeManager().hasDataType(loc)) {
                    factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                    return;
                }
            }

            // Old block is lower than new block
            loc = event.getBlock().getLocation();
            loc.setZ(loc.getZ() + 1);
            if (event.getBlock().getWorld().getBlockAt(loc).getType() == Material.AIR) {
                loc.setY(loc.getY() - 1);
                if (factory.getDataTypeManager().hasDataType(loc)) {
                    factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                    return;
                }
            }

            loc = event.getBlock().getLocation();
            loc.setZ(loc.getZ() - 1);
            if (event.getBlock().getWorld().getBlockAt(loc).getType() == Material.AIR) {
                loc.setY(loc.getY() - 1);
                if (factory.getDataTypeManager().hasDataType(loc)) {
                    factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                    return;
                }
            }

            loc = event.getBlock().getLocation();
            loc.setX(loc.getX() + 1);
            if (event.getBlock().getWorld().getBlockAt(loc).getType() == Material.AIR) {
                loc.setY(loc.getY() - 1);
                if (factory.getDataTypeManager().hasDataType(loc)) {
                    factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                    return;
                }
            }

            loc = event.getBlock().getLocation();
            loc.setX(loc.getX() - 1);
            if (event.getBlock().getWorld().getBlockAt(loc).getType() == Material.AIR) {
                loc.setY(loc.getY() - 1);
                if (factory.getDataTypeManager().hasDataType(loc)) {
                    factory.getDataTypeManager().carryDataType(loc, event.getBlock().getLocation());
                    return;
                }
            }

            return;
        }
    }
}
