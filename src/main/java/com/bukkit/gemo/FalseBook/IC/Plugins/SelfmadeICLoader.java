package com.bukkit.gemo.FalseBook.IC.Plugins;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.plugin.PluginDescriptionFile;

public class SelfmadeICLoader
{
  protected final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
  protected final Map<String, SelfmadeICClassLoader> loaders = new HashMap<String, SelfmadeICClassLoader>();

  public ExternalICPackage loadPlugin(File file) {
    ExternalICPackage result = null;
    PluginDescriptionFile description = null;

    if (!file.exists()) {
      FalseBookICCore.printInConsole("ERROR: plugin '" + file.getName() + "' not found!");
      return result;
    }
    try
    {
      JarFile jar = new JarFile(file);
      JarEntry entry = jar.getJarEntry("plugin.yml");
      if (entry == null) {
        FalseBookICCore.printInConsole("Jar '" + file.getName() + "' does not contain plugin.yml");
      }
      InputStream stream = jar.getInputStream(entry);
      description = new PluginDescriptionFile(stream);
      stream.close();
      jar.close();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    SelfmadeICClassLoader loader = null;
    try {
      URL[] urls = new URL[1];
      urls[0] = file.toURI().toURL();
      loader = new SelfmadeICClassLoader(this, urls, getClass().getClassLoader());
      Class jarClass = Class.forName(description.getMain(), true, loader);
      if ((jarClass.newInstance() instanceof ExternalICPackage))
      {
        Class plugin = jarClass.asSubclass(ExternalICPackage.class);
        Constructor constructor = plugin.getConstructor(new Class[0]);
        result = (ExternalICPackage)constructor.newInstance(new Object[0]);
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    this.loaders.put(description.getName(), loader);
    return result;
  }

  public Class<?> getClassByName(String name) {
    Class cachedClass = this.classes.get(name);

    if (cachedClass != null) {
      return cachedClass;
    }
    for (String current : this.loaders.keySet()) {
      SelfmadeICClassLoader loader = this.loaders.get(current);
      try
      {
        cachedClass = loader.findClass(name, false);
      } catch (ClassNotFoundException localClassNotFoundException) {
      }
      if (cachedClass != null) {
        return cachedClass;
      }
    }

    return null;
  }

  public void setClass(String name, Class<?> clazz) {
    if (!this.classes.containsKey(name))
      this.classes.put(name, clazz);
  }
}