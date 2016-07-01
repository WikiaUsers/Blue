package com.blue.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Loads configuration and account data
 * TODO: Make it load data about plugin JAR file locations
 */
public class DataManager {
	
	/**
	 * Initializer
	 */
	public static void init(String config) {
		loadAccounts();
		loadConfig(config);
	}
	
	/**
	 * Loads JSON file from memory
	 * @param  filename        JSON file name
	 * @return                 JSON data from file formatted in {@code JSONObject}
	 * @throws  IOException    if file isn't found
	 * @throws  JSONException  if there was an error while parsing JSON data in the file
	 */
	private static JSONObject loadJSONFile(String filename) throws IOException, JSONException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String json = "", line = "";
		while((line = br.readLine()) != null) json += line;
		br.close();
		return new JSONObject(json);
	}
	
	/**
	 * Loads configuration file, and passes room data to {@code RoomManager}
	 * and plugin data to {@code PluginManager}
	 * @param  config  configuration file to load,
	 *                 prefixed with <code>config</code>
	 *                 and suffixed with <code>.json</code>
	 */
	private static void loadConfig(String config) {
		try {
			JSONObject result = loadJSONFile("config-" + config + ".json");
		
			// Iterating through the configuration file
			Iterator<?> keys = result.keys();
		
			while(keys.hasNext()) {
				String subdomain = (String)keys.next();
				JSONObject data = result.getJSONObject(subdomain);
				
				// Loading plugins from object
				JSONObject plugins = data.getJSONObject("plugins");
				Iterator<?> keys2 = plugins.keys();
				Map<String, JSONObject> pluginMap = new HashMap<String, JSONObject>();
				while(keys2.hasNext()) {
					String key2 = (String)keys2.next();
					pluginMap.put(key2, plugins.getJSONObject(key2));
				}
				PluginManager.registerPlugins(subdomain, pluginMap);
				
				// Opening a new Room
				RoomManager.addMainRoom(subdomain, data.getString("account"));
			}
		}
		catch(JSONException e) { e.printStackTrace(); }
		catch(IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Loads data about accounts from <code>accounts.json</code> file
	 * and passes data to {@code AccountManager}
	 */
	private static void loadAccounts() {
		try {
			JSONObject result = loadJSONFile("accounts.json");
			
			// Iterating through the accounts file
			Iterator<?> keys = result.keys();
			
			while(keys.hasNext()) {
				String key = (String)keys.next();
				if(result.get(key) instanceof String) AccountManager.registerNewAccount(key, result.getString(key));
			}
		}
		catch(JSONException e) { e.printStackTrace(); }
		catch(IOException e) { e.printStackTrace(); }
	}
	
}
