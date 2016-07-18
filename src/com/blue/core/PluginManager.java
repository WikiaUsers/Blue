package com.blue.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * Manages plugins
 * TODO: Fix lazy documentation
 */
public class PluginManager {
	
	private static Map<String, Plugin[]> plugins;
	
	/**
	 * Initializer
	 */
	public static void init() {
		plugins = new HashMap<String, Plugin[]>();
	}
	
	/**
	 * Initializes all plugins with their configurations
	 * @param  subdomain  subdomain of the wiki to register plugins for
	 * @param  plugins    map of plugin names and configurations
	 */
	public static void registerPlugins(String subdomain, Map<String, JSONObject> plugins) {
		for(Object a : plugins.keySet().toArray()) {
			String key = (String)a;
			JSONObject config = plugins.get(key);
			// TODO: Actually initialize plugin
		}
	}
	
	/**
	 * Returns plugins that certain wiki uses
	 * @param  subdomain  the subdomain of the wiki whose plugins to return
	 * @return            plugins of the specified wiki
	 */
	public static Plugin[] getPluginsBySubdomain(String subdomain) { /* TODO: Implement */ return null; }
	
}
