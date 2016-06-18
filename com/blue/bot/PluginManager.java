package com.blue.bot;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Policy;

class PluginManager {
    public Plugin[] plugins;

    public void PluginManager() {
        Policy.setPolicy(new PluginPolicy());
		System.setSecurityManager(new SecurityManager());
    }

    public void loadPlugins() {

    }

    public void loadPlugin() {

    }

    public void executePlugins() {

    }

    public void executePlugin() {

    }
}
