package com.grover.mingebag.ic;

public abstract class BaseData {

    public abstract DataTypes getType();

    public abstract boolean compare(BaseData data, String comparison);
}
