package com.grover.mingebag.ic;

public class DataType {
	private byte type;
	private BaseData data;
	
	public DataType(BaseData data, byte type) {
		this.data = data;
		this.type = type;
	}
	
	public byte getType() {
		return type;
	}
	
	public BaseData getData() {
		return this.data;
	}
	
	public String toString() {
		if(this.data != null) {
			return "Type: " + this.type + "; Data: " + this.data.toString();
		}
		return  "Type: " + this.type;
	}
}
