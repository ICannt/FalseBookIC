package com.bukkit.gemo.FalseBook.IC.ICs;

import com.bukkit.gemo.utils.ICUtils;
import org.bukkit.block.Sign;

public class InputState {

    private boolean[] input;

    public InputState(Sign sign) {
        this.input = new boolean[3];
        this.input[0] = ICUtils.isInputHigh(sign, 1);
        this.input[1] = ICUtils.isInputHigh(sign, 2);
        this.input[2] = ICUtils.isInputHigh(sign, 3);
    }

    public boolean isInputOneHigh() {
        return this.input[0];
    }

    public boolean isInputTwoHigh() {
        return this.input[1];
    }

    public boolean isInputThreeHigh() {
        return this.input[2];
    }

    public boolean isInputOneLow() {
        return !isInputOneHigh();
    }

    public boolean isInputTwoLow() {
        return !isInputTwoHigh();
    }

    public boolean isInputThreeLow() {
        return !isInputThreeHigh();
    }
}