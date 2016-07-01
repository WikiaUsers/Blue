package com.blue.core;

import org.json.JSONObject;

/**
 * Plugin class that all other plugins will have to extend.
 * @see {@code PluginInterface}
 */
public abstract class Plugin implements PluginInterface {
	
	/**
	 * Class constructor
	 * @param  config  configuration of the plugin
	 */
	public Plugin(JSONObject config) { }
	
}