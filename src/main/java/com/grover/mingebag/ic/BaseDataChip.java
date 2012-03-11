package com.grover.mingebag.ic;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.SignUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;



public class BaseDataChip extends BaseIC
{
	
	public void outputData(BaseData data, DataTypes type, final Sign signBlock, final int distance, final int pulse) {
		DataTypeManager manager = this.core.getFactory().getDataTypeManager();
		manager.addDataType(ICUtils.getLeverPos(signBlock, distance), new DataType(data, type));
		switchLever(Lever.BACK, signBlock, true, distance);
		if(pulse > 0) {
			this.core.getServer().getScheduler().scheduleSyncDelayedTask(this.core, new Runnable() {
			    public void run() {
			        switchLever(Lever.BACK, signBlock, false, distance);
			    }
			}, pulse);
		}
		manager.endDataType(ICUtils.getLeverPos(signBlock, distance));
	}
	
	public void outputDataLeft(BaseData data, DataTypes type, final Sign signBlock, final int distance, final int pulse) {
		DataTypeManager manager = this.core.getFactory().getDataTypeManager();
		manager.addDataType(ICUtils.getLeverPosLeft(signBlock, distance), new DataType(data, type));
		switchLever(Lever.LEFT, signBlock, true, distance);
		if(pulse > 0) {
			this.core.getServer().getScheduler().scheduleSyncDelayedTask(this.core, new Runnable() {
			    public void run() {
			        switchLever(Lever.LEFT, signBlock, false, distance);
			    }
			}, pulse);
		}
		manager.endDataType(ICUtils.getLeverPosLeft(signBlock, distance));
	}
	
	public void outputDataRight(BaseData data, DataTypes type, final Sign signBlock, final int distance, final int pulse) {
		DataTypeManager manager = this.core.getFactory().getDataTypeManager();
		manager.addDataType(ICUtils.getLeverPosRight(signBlock, distance), new DataType(data, type));
		switchLever(Lever.RIGHT, signBlock, true, distance);
		if(pulse > 0) {
			this.core.getServer().getScheduler().scheduleSyncDelayedTask(this.core, new Runnable() {
			    public void run() {
			        switchLever(Lever.RIGHT, signBlock, false, distance);
			    }
			}, pulse);
		}
		manager.endDataType(ICUtils.getLeverPosRight(signBlock, distance));
	}
	
	public BaseData getData(Sign signBlock) {
		int direction = SignUtils.getDirection(signBlock);
		DataType type = null;
		Location loc = signBlock.getLocation().clone();
		if(direction == 1) {
			loc.setZ(loc.getZ()+1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 2) {
			loc.setX(loc.getX()+1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 3) {
			loc.setZ(loc.getZ()-1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 4) {
			loc.setX(loc.getX()-1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(type == null) {
			return null;
		}
		return type.getData();
	}
	
	public BaseData getDataLeft(Sign signBlock) {
		int direction = SignUtils.getDirection(signBlock);
		DataType type = null;
		Location loc = signBlock.getLocation().clone();
		if(direction == 1) {
			loc.setX(loc.getX()+1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 2) {
			loc.setZ(loc.getZ()+1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 3) {
			loc.setX(loc.getX()-1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 4) {
			loc.setX(loc.getZ()-1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(type == null) {
			return null;
		}
		return type.getData();
	}
	
	public BaseData getDataRight(Sign signBlock) {
		int direction = SignUtils.getDirection(signBlock);
		DataType type = null;
		Location loc = signBlock.getLocation().clone();
		if(direction == 1) {
			loc.setX(loc.getX()-1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 2) {
			loc.setZ(loc.getZ()-1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 3) {
			loc.setX(loc.getX()+1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(direction == 4) {
			loc.setX(loc.getZ()+1d);
			type = core.getFactory().getDataTypeManager().getDataType(loc);
		}
		if(type == null) {
			return null;
		}
		return type.getData();
	}
	
	public BaseData[] getDefaults(Sign block) {
		Location loc = block.getLocation().clone();
		loc.setY(loc.getY()-1d);
		Block defaults = block.getWorld().getBlockAt(loc);
		BaseData[] data = new BaseData[3];
		if(defaults.getType() == Material.WALL_SIGN) {
			if(defaults.getState() instanceof Sign) {
				Sign defaultSign = (Sign) defaults.getState();
				if(defaultSign.getLine(0).equals("[defaults]")) {
					// Number, String
					for(int i = 0; i <= 2; i++) {
						data[i] = getDefaultData(defaultSign.getLine(i));
					}
				}
			}
		}
		return data;
	}
	
	public BaseData getDefaultData(String text) {
		try {
			return new NumberData(Integer.parseInt(text));
		} catch(Exception e) {
		}	
		return new StringData(text);
	}
}