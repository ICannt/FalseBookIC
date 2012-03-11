package com.bukkit.gemo.FalseBook.IC;

public class DelayedDataLoader
  implements Runnable
{
  private PersistenceHandler persistence;

  public DelayedDataLoader(PersistenceHandler persistence)
  {
    this.persistence = persistence;
  }

  public void run() {
    this.persistence.loadSelftriggeredICs();
  }
}