package com.bukkit.gemo.FalseBook.IC.ExecutionEvents;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;

public class ICRunningTask
        implements Runnable {

    private int maxExecutions = 500;
    private int ExeTaskID = -1;
    private ArrayList<ICExecutionEvent> queuedICs = new ArrayList<ICExecutionEvent>();
    private HashMap<String, Integer> queuedICsPos = new HashMap<String, Integer>();
    private FalseBookICCore plugin = null;

    public ICRunningTask(FalseBookICCore instance) {
        this.plugin = instance;
    }

    public void run() {
        synchronized (this.queuedICs) {
            if (this.queuedICs.size() == 0) {
                this.ExeTaskID = -1;
                return;
            }

            for (int i = this.queuedICs.size() - 1; i >= 0; i--) {
                try {
                    this.queuedICs.get(i).Execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.queuedICsPos.remove(this.queuedICs.get(i).getSignBlock().getLocation().toString());
                this.queuedICs.remove(i);
            }

            this.ExeTaskID = -1;

            if ((this.ExeTaskID == -1) && (this.queuedICs.size() > 0)) {
                this.ExeTaskID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 1L);
            }
        }
    }

    public int getMaxExecutions() {
        return this.maxExecutions;
    }

    public void setMaxExecutions(int maxExecutions) {
        this.maxExecutions = maxExecutions;
    }

    public int getExeTaskID() {
        return this.ExeTaskID;
    }

    public void setExeTaskID(int exeTaskID) {
        this.ExeTaskID = exeTaskID;
    }

    public ArrayList<ICExecutionEvent> getQueuedICs() {
        return this.queuedICs;
    }

    public void setQueuedICs(ArrayList<ICExecutionEvent> queuedICs) {
        this.queuedICs = queuedICs;
    }

    public HashMap<String, Integer> getQueuedICsPos() {
        return this.queuedICsPos;
    }

    public void setQueuedICsPos(HashMap<String, Integer> queuedICsPos) {
        this.queuedICsPos = queuedICsPos;
    }
}