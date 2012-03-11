package com.grover.mingebag.ic;

import org.bukkit.entity.Player;


public class PlayerData extends BaseData {
	Player player;
	
	public PlayerData(Player player) {
		this.player = player;
	}

	@Override
	public DataTypes getType() {
		return DataTypes.PLAYER;
	}
	
	@Override
	public boolean compare(BaseData data, String comparison) {
		if(data instanceof PlayerData) {
			PlayerData pdata = (PlayerData) data;
			
			if(comparison.equals("==")) {
				if(this.player.getPlayer().getName().equals(pdata.player.getName())) {
					return true;
				}
			}
			
			if(comparison.equals("!=")) {
				if(!this.player.getPlayer().getName().equals(pdata.player.getName())) {
					return true;
				}
			}
		}
		return false;
	}
}
