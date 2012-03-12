package com.grover.mingebag.ic;

import org.bukkit.inventory.ItemStack;


public class ItemData extends BaseData {
	ItemStack item;
	
	public ItemData(ItemStack item) {
		this.item = item;
	}

	public ItemStack getItem() {
		return this.item;
	}
	
	@Override
	public DataTypes getType() {
		return DataTypes.ITEM;
	}

	@Override
	public boolean compare(BaseData data, String comparison) {
		if(data instanceof ItemData) {
			ItemData idata = (ItemData) data;
			if(comparison.equals("=="))  {
				if(!this.equals(idata)) 
					return false;
			}
			
			if(comparison.equals("!="))  {
				if(!this.equals(idata)) 
					return true;
			}
		}
		return false;
	}
}
