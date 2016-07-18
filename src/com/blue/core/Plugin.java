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
	
	/* Useless */
	@Override
	public void onMessage(Room room, User user, String message) { }
	@Override
	public void onInitial(Room room) { }
	@Override
	public void onJoin(Room room, User user) { }
	@Override
	public void onPart(Room room, User user) { }
	@Override
	public void onLogout(Room room, User user) { }
	@Override
	public void onKick(Room room, User user, User executor) { }
	@Override
	public void onBan(Room room, User user, User executor, String reason, String length) { }
	@Override
	public void onRoomExit(Room room) { }
	@Override
	public void onUpdateUser(Room room, User user) { }
	
}