package com.bukkit.gemo.FalseBook.IC;

import com.bukkit.gemo.commands.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.bukkit.Location;

public class DatabaseHandler
{
  private final DatabaseConnection conHandler;

  public DatabaseHandler(String host, int port, String database, String userName, String password)
  {
    this.conHandler = new DatabaseConnection(host, port, database, userName, password);

    host = null;
    port = 0;
    database = null;
    userName = null;
    password = null;
    System.gc();
    try
    {
      init();
    } catch (Exception e) {
      FalseBookICCore.printInConsole("ERROR! Can't initialize MySQL!");
      e.printStackTrace();
    }
  }

  public DatabaseHandler(String folder, String fileName)
  {
    this.conHandler = new DatabaseConnection(folder, fileName);
    System.gc();
    try {
      init();
    } catch (Exception e) {
      FalseBookICCore.printInConsole("ERROR! Can't initialize SQLite!");
      e.printStackTrace();
    }
  }

  private void init() throws Exception {
    checkTables();
  }

  private void checkTables()
    throws Exception
  {
    Connection con = this.conHandler.getConnection();

    if (!FalseBookICCore.getInstance().isUseMySQL()) {
      con.createStatement().execute(
        "CREATE TABLE IF NOT EXISTS `SensorICs` (`Id` INT PRIMARY_KEY,`SensorId` INT,`WorldName` VARCHAR(255),`SignX` INT,`SignY` INT,`SignZ` INT);");
    }
    else
    {
      con.createStatement().execute(
        "CREATE TABLE IF NOT EXISTS SensorICs (`Id` INT NOT NULL , `SensorId` INT NOT NULL, `WorldName` VARCHAR( 255 ) NOT NULL ,`SignX` INT NOT NULL ,`SignY` INT NOT NULL ,`SignZ` INT NOT NULL );");
    }
  }

  public void closeConnection()
  {
    this.conHandler.closeConnection();
  }

  public ResultSet getAllICs() {
    try {
      Statement statement = this.conHandler.getConnection().createStatement();
      return statement.executeQuery("Select * FROM SensorICs;");
    } catch (Exception e) {
      FalseBookICCore.printInConsole("----------------------------------------");
      FalseBookICCore.printInConsole("ERROR while executing SQL: getAllICs()!");
      FalseBookICCore.printInConsole("----------------------------------------");
      e.printStackTrace();
      FalseBookICCore.printInConsole("----------------------------------------");
    }
    return null;
  }

  public void deleteAllICs()
  {
    try
    {
      Statement statement = this.conHandler.getConnection().createStatement();
      statement.executeUpdate("DELETE FROM SensorICs;");
    } catch (Exception e) {
      FalseBookICCore.printInConsole("----------------------------------------");
      FalseBookICCore.printInConsole("ERROR while executing SQL: removeAllICs()!");
      FalseBookICCore.printInConsole("----------------------------------------");
      e.printStackTrace();
      FalseBookICCore.printInConsole("----------------------------------------");
    }
  }

  public void removeSelftriggeredIC(int SensorID)
  {
    try
    {
      Statement statement = this.conHandler.getConnection().createStatement();
      statement.executeUpdate("DELETE FROM SensorICs WHERE Id=" + SensorID + ";");
    } catch (Exception e) {
      FalseBookICCore.printInConsole("----------------------------------------");
      FalseBookICCore.printInConsole("ERROR while executing SQL: removeSelftriggeredIC(int SensorID)!");
      FalseBookICCore.printInConsole("----------------------------------------");
      e.printStackTrace();
      FalseBookICCore.printInConsole("----------------------------------------");
    }
  }

  public void removeSelftriggeredIC(Location location)
  {
    try
    {
      Statement statement = this.conHandler.getConnection().createStatement();
      statement.executeUpdate("DELETE FROM SensorICs WHERE WorldName='" + location.getWorld().getName() + "' AND SignX=" + location.getBlockX() + " AND SignY=" + location.getBlockY() + " AND SignZ=" + location.getBlockZ() + ";");
    } catch (Exception e) {
      FalseBookICCore.printInConsole("----------------------------------------");
      FalseBookICCore.printInConsole("ERROR while executing SQL: removeSelftriggeredIC(Location location)!");
      FalseBookICCore.printInConsole("----------------------------------------");
      e.printStackTrace();
      FalseBookICCore.printInConsole("----------------------------------------");
    }
  }

  public int getNextID()
  {
    try
    {
      Statement statement = this.conHandler.getConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT Id FROM SensorICs ORDER BY Id DESC LIMIT 1");
      if (result.next()) {
        return result.getInt("Id");
      }
      return 1;
    } catch (Exception e) {
      FalseBookICCore.printInConsole("----------------------------------------");
      FalseBookICCore.printInConsole("Error while executing SQL: getHighestID()!");
      FalseBookICCore.printInConsole("----------------------------------------");
      e.printStackTrace();
      FalseBookICCore.printInConsole("----------------------------------------");
    }
    return 1;
  }

  public boolean ICExists(Location location)
  {
    try
    {
      Statement statement = this.conHandler.getConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM SensorICs WHERE WorldName='" + location.getWorld().getName() + "' AND SignX=" + location.getBlockX() + " AND SignY=" + location.getBlockY() + " AND SignZ=" + location.getBlockZ() + ";");

      return result.next();
    }
    catch (Exception e)
    {
      FalseBookICCore.printInConsole("----------------------------------------");
      FalseBookICCore.printInConsole("Error while executing SQL: ICExists(Location location)!");
      FalseBookICCore.printInConsole("----------------------------------------");
      e.printStackTrace();
      FalseBookICCore.printInConsole("----------------------------------------");
    }
    return false;
  }

  public boolean addIC(int SensorID, Location location)
  {
    try
    {
      int nextID = getNextID();
      Statement statement = this.conHandler.getConnection().createStatement();
      return statement.executeUpdate("INSERT INTO SensorICs VALUES (" + nextID + ", " + SensorID + ", '" + location.getWorld().getName() + "', " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")") == 1;
    } catch (Exception e) {
      FalseBookICCore.printInConsole("----------------------------------------");
      FalseBookICCore.printInConsole("Error while executing SQL: addIC(int SensorID, Location location)!");
      FalseBookICCore.printInConsole("----------------------------------------");
      e.printStackTrace();
      FalseBookICCore.printInConsole("----------------------------------------");
    }return false;
  }
}