package com.grover.mingebag.ic;


public class StringData extends BaseData {
	String string;
	
	public StringData(String string) {
		this.string = string;
	}

	public String getString() {
		return this.string;
	}
	@Override
	public byte getType() {
		return DataTypes.STRING.id;
	}

	@Override
	public boolean compare(BaseData data, String comparison) {
		if(data instanceof StringData) {
			StringData sdata = (StringData) data;
			if(comparison.equals("==")) {
				if(sdata.getString().equals(this.string)) {
					return true;
				}
			}
			
			if(comparison.equals("!=")) {
				if(!sdata.getString().equals(this.string)) {
					return true;
				}
			}
		}
		return false;
	}
}
