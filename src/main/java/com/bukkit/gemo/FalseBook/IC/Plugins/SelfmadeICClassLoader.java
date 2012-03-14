package com.bukkit.gemo.FalseBook.IC.Plugins;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SelfmadeICClassLoader extends URLClassLoader {

    private final SelfmadeICLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();

    public SelfmadeICClassLoader(SelfmadeICLoader loader, URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.loader = loader;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        Class result = this.classes.get(name);
        if (result == null) {
            if (checkGlobal) {
                result = this.loader.getClassByName(name);
            }
            if (result == null) {
                result = super.findClass(name);

                if (result != null) {
                    this.loader.setClass(name, result);
                }
            }
            this.classes.put(name, result);
        }
        return result;
    }

    public Set<String> getClasses() {
        return this.classes.keySet();
    }
}