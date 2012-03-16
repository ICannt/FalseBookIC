package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.commands.Command;
import java.io.File;
import java.io.FileWriter;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;

public class cmdExportJSON extends Command {

    public cmdExportJSON(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Export the ICs to JSON";
    }
    
    @Override
    public void execute(String[] paramArrayOfString, CommandSender paramCommandSender) {
        ICFactory factory = FalseBookICCore.getInstance().getFactory();
        
        JSONObject root = new JSONObject();
        
        for(SelftriggeredBaseIC ic : factory.getRegisteredSTICs().values()) {
            JSONObject obj = new JSONObject();
            
            obj.put("type", "self-triggered");
            obj.put("node", ic.ICNumber);
            obj.put("name", ic.ICName);
            obj.put("group", ic.Group);
            obj.put("description", ic.ICDescription);
            obj.put("sign-depth", (Integer)((int)ic.ICSignDepth));
            
            obj.put("input-count", ic.chipState.getInputCount());
            obj.put("input-1", ic.chipState.getNameInput1());
            obj.put("input-2", ic.chipState.getNameInput2());
            obj.put("input-3", ic.chipState.getNameInput3());
            
            obj.put("output-count", ic.chipState.getOutputCount());
            obj.put("output-1", ic.chipState.getNameOutput1());
            obj.put("output-2", ic.chipState.getNameOutput2());
            obj.put("output-3", ic.chipState.getNameOutput3());

            root.put(ic.ICNumber.toLowerCase(), obj);
        }
        
        for(BaseIC ic : factory.getRegisteredTICs().values()) {
            JSONObject obj = new JSONObject();
            
            obj.put("type", "redstone-triggered");
            obj.put("node", ic.ICNumber);
            obj.put("name", ic.ICName);
            obj.put("group", ic.Group);
            obj.put("description", ic.ICDescription);
            obj.put("sign-depth", (Integer)((int)ic.ICSignDepth));
            
            obj.put("input-count", ic.chipState.getInputCount());
            obj.put("input-1", ic.chipState.getNameInput1());
            obj.put("input-2", ic.chipState.getNameInput2());
            obj.put("input-3", ic.chipState.getNameInput3());
            
            obj.put("output-count", ic.chipState.getOutputCount());
            obj.put("output-1", ic.chipState.getNameOutput1());
            obj.put("output-2", ic.chipState.getNameOutput2());
            obj.put("output-3", ic.chipState.getNameOutput3());

            root.put(ic.ICNumber.toLowerCase(), obj);
        }
        
        try {
            File file = new File("plugins/FalseBook/ic-export.json");
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(root.toJSONString());
            writer.flush();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
