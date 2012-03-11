package com.bukkit.gemo.FalseBook.IC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class WikiPage
{
  ArrayList<String> lines = new ArrayList<String>();

  public WikiPage(String template) {
    try {
      File thisFile = new File(template);
      if (!thisFile.exists()) {
        return;
      }
      BufferedReader in = new BufferedReader(new FileReader(template));
      String zeile = "";
      while ((zeile = in.readLine()) != null) {
        this.lines.add(zeile.trim());
      }

      in.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void replaceText(String placeHolder, String newText) {
    String line = "";
    for (int i = 0; i < this.lines.size(); i++) {
      line = this.lines.get(i);
      if (line.contains(placeHolder))
        this.lines.set(i, line.replace(placeHolder, newText));
    }
  }

  public void savePage(String fileName)
  {
    try {
      File datei = new File(fileName);
      if (datei.exists()) {
        datei.delete();
      }
      File savedFile = new File(fileName);
      FileWriter writer = new FileWriter(savedFile, false);
      for (int i = 0; i < this.lines.size(); i++) {
        writer.write(this.lines.get(i) + System.getProperty("line.separator"));
      }
      writer.flush();
      writer.close();
    } catch (Exception e) {
      FalseBookICCore.printInConsole("Error while writing Wikipage: " + fileName);
    }
  }
}