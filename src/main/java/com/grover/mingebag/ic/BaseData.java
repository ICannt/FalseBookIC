package com.grover.mingebag.ic;

public abstract class BaseData {
	public abstract byte getType();
	public abstract boolean compare(BaseData data, String comparison);
}
