package com.blue.core;

public class Blue {
	
	/**
	 * Main method of the plugin
	 * @param  args  command-line arguments that are received
	 */
	public static void main(String[] args) {
		// TODO: Parsing command-line arguments
		new Blue();
	}
	
	/**
	 * Class constructor
	 */
	public Blue() {
		AccountManager.init();
		RoomManager.init();
		PluginManager.init();
		DataManager.init("1");
	}
}