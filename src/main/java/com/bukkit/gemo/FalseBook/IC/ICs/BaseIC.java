package com.bukkit.gemo.FalseBook.IC.ICs;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class BaseIC
        implements IC {

    public FalseBookICCore core;
    public Sign signBlock;
    public String ICName = null;
    public String ICNumber = null;
    public ICGroup Group = null;
    public String ICDescription = "";
    public BaseChip chipState = null;
    public byte ICSignDepth = 0;

    protected BaseIC(FalseBookICCore core) {
        this.core = core;
    }

    public BaseIC() {
    }

    public boolean hasPermission(Player player) {
        return (UtilPermissions.playerCanUseCommand(player, "falsebook.ic." + this.Group.name().toLowerCase())) || (UtilPermissions.playerCanUseCommand(player, "falsebook.ic." + this.ICNumber.toLowerCase().substring(1, this.ICNumber.length() - 1))) || (UtilPermissions.playerCanUseCommand(player, "falsebook.anyic"));
    }

    public final void RawExecute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
        if (ICSignDepth == 0) {
            Execute(signBlock, currentInputs, previousInputs);
        } else {
            ArrayList<Sign> signs = new ArrayList<Sign>();
            signs.add(signBlock);

            Location loc = signBlock.getLocation();
            int depth = ICSignDepth + 1;
            for (int i = 1; i < depth; ++i) {
                Location newLoc = loc.clone().subtract(0, i, 0);
                Block block = newLoc.getBlock();
                if (block.getType() == Material.WALL_SIGN) {
                    signs.add((Sign) block.getState());
                }
                else {
                    break;
                }
            }

            Execute(signs, currentInputs, previousInputs);
        }
    }

    public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
    }

    public void Execute(ArrayList<Sign> signBlock, InputState currentInputs, InputState previousInputs) {
    }

    public void Execute() {
        if ((this.signBlock != null)
                && (this.core.isLoadUnloadedChunks())
                && (!this.signBlock.getChunk().isLoaded())) {
            this.signBlock.getWorld().loadChunk(this.signBlock.getChunk().getX(), this.signBlock.getChunk().getZ());
        }
    }

    public boolean validateIC() {
        if (this.signBlock == null) {
            return false;
        }
        if (this.signBlock.getTypeId() != Material.WALL_SIGN.getId()) {
            return false;
        }
        if ((!this.core.isLoadUnloadedChunks())
                && (!this.signBlock.getChunk().isLoaded())) {
            return false;
        }

        if (this.signBlock.getLine(0).length() < 1) {
            this.signBlock = ((Sign) this.signBlock.getWorld().getBlockAt(this.signBlock.getLocation()).getState());
        }
        return this.ICNumber.equalsIgnoreCase(this.signBlock.getLine(0));
    }

    public void checkCreation(SignChangeEvent event) {
    }

    public boolean onBreakByPlayer(Player player, Sign signBlock) {
        return true;
    }

    public void onBreakByExplosion(Sign signBlock) {
    }

    public void onRightClick(Player player, Sign signBlock) {
    }

    public void onLeftClick(Player player, Sign signBlock) {
    }

    public void notifyCreationSuccess(Player player) {
        ChatUtils.printSuccess(player, "[FB-IC]", this.ICName + " created.");
    }

    public static Location getICBlock(Sign signBlock) {
        Location leverPos = signBlock.getBlock().getLocation().clone();

        switch (signBlock.getRawData()) {
            case 2:
                leverPos.setZ(leverPos.getZ() + 1.0D);
                break;
            case 3:
                leverPos.setZ(leverPos.getZ() - 1.0D);
                break;
            case 4:
                leverPos.setX(leverPos.getX() + 1.0D);
                break;
            case 5:
                leverPos.setX(leverPos.getX() - 1.0D);
        }

        return leverPos;
    }

    public static Location getICBlock(Sign signBlock, Vector vector) {
        Location position = signBlock.getBlock().getLocation().clone();

        switch (signBlock.getRawData()) {
            case 2:
                position.setZ(position.getZ() + 1.0D + vector.getBlockZ());
                position.setX(position.getX() - vector.getBlockX());
                break;
            case 3:
                position.setZ(position.getZ() - 1.0D - vector.getBlockZ());
                position.setX(position.getX() + vector.getBlockX());
                break;
            case 4:
                position.setX(position.getX() + 1.0D + vector.getBlockZ());
                position.setZ(position.getZ() + vector.getBlockX());
                break;
            case 5:
                position.setX(position.getX() - 1.0D - vector.getBlockZ());
                position.setZ(position.getZ() - vector.getBlockX());
        }

        position.setY(position.getY() + vector.getBlockY());
        return position;
    }

    public void setSignBlock(Sign signBlock) {
        this.signBlock = signBlock;
    }

    public Sign getSignBlock() {
        return this.signBlock;
    }

    public ICGroup getICGroup() {
        return this.Group;
    }

    public void setICGroup(ICGroup icgroup) {
        this.Group = icgroup;
    }

    public String getICDescription() {
        return this.ICDescription;
    }

    public void setICDescription(String iCDescription) {
        this.ICDescription = iCDescription;
    }

    public BaseChip getChipState() {
        return this.chipState;
    }

    public void setChipState(BaseChip chipState) {
        this.chipState = chipState;
    }

    public String getICName() {
        return this.ICName;
    }

    public void setICName(String ICName) {
        this.ICName = ICName;
    }

    public String getICNumber() {
        return this.ICNumber;
    }

    public void setICNumber(String ICNumber) {
        this.ICNumber = ICNumber;
    }

    public void onImport() {
    }

    public void initCore() {
        this.core = FalseBookICCore.getInstance();
    }

    protected void switchLever(Lever lever, Sign signBlock, boolean newStatus) {
        switch (lever) {
            case BACK:
                ICUtils.switchLever(signBlock, newStatus);
                break;
            case LEFT:
                ICUtils.switchLeverLeft(signBlock, newStatus);
                break;
            case RIGHT:
                ICUtils.switchLeverRight(signBlock, newStatus);
        }
    }

    protected void switchLever(Lever lever, Sign signBlock, boolean newStatus, int distance) {
        switch (lever) {
            case BACK:
                ICUtils.switchLever(signBlock, newStatus, distance);
                break;
            case LEFT:
                ICUtils.switchLeverLeft(signBlock, newStatus, distance);
                break;
            case RIGHT:
                ICUtils.switchLeverRight(signBlock, newStatus, distance);
        }
    }

    public void setICSignDepth(byte ICSignDepth) {
        this.ICSignDepth = ICSignDepth;
    }
}