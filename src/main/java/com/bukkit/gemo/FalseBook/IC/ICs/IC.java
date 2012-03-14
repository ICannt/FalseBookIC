package com.bukkit.gemo.FalseBook.IC.ICs;

import java.util.ArrayList;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public abstract interface IC {

    public abstract boolean hasPermission(Player paramPlayer);

    public abstract void checkCreation(SignChangeEvent paramSignChangeEvent);

    public abstract void onImport();

    public abstract void Execute();

    public abstract void RawExecute(Sign paramSign, InputState paramInputState1, InputState paramInputState2);

    public abstract void Execute(Sign paramSign, InputState paramInputState1, InputState paramInputState2);

    public abstract void Execute(ArrayList<Sign> paramSign, InputState paramInputState1, InputState paramInputState2);
}