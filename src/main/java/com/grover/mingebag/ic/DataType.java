package com.grover.mingebag.ic;

public class DataType {

    private DataTypes type;
    private BaseData data;

    public DataType(BaseData data) {
        this.data = data;
        this.type = data.getType();
    }

    public DataTypes getType() {
        return type;
    }

    public BaseData getData() {
        return this.data;
    }

    public String toString() {
        if (this.data != null) {
            return "Type: " + this.type + "; Data: " + this.data.toString();
        }
        return "Type: " + this.type;
    }
}
