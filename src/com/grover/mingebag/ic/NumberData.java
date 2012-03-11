package com.grover.mingebag.ic;


public class NumberData extends BaseData {
	int number;
	
	public NumberData(int number) {
		this.number = number;
	}

	public int getInt() {
		return this.number;
	}
	
	@Override
	public byte getType() {
		return DataTypes.NUMBER.id;
	}

	@Override
	public boolean compare(BaseData data, String comparison) {
		if(data instanceof NumberData) {
			NumberData ndata = (NumberData) data;
			if(comparison.equals("==")) {
				if(ndata.getInt() == getInt()) {
					return true;
				}
			}
			
			if(comparison.equals("!=")) {
				if(ndata.getInt() != getInt()) {
					return true;
				}
			}
			
			if(comparison.equals(">")) {
				if(ndata.getInt() > getInt()) {
					return true;
				}
			}
			
			if(comparison.equals("<")) {
				if(ndata.getInt() < getInt()) {
					return true;
				}
			}
			
			if(comparison.equals(">=")) {
				if(ndata.getInt() >= getInt()) {
					return true;
				}
			}
			
			if(comparison.equals("<=")) {
				if(ndata.getInt() <= getInt()) {
					return true;
				}
			}
		}
		return false;
	}
}
