package com.blue.core;

class ConnectionManager {
	private Connector connector;
	private Plugin[] plugins;
	
	public ConnectionManager(Connector connector, Plugin[] plugins) {
		this.connector = connector;
		this.plugins = plugins;
	}
	
	public void send(String message) {
		this.connector.send(message);
	}
	
	public void run() {
		if (this.connector.hasMessages() == true) {
			String[] messages = this.connector.getMessages();
			
			for (int i = 0; i < this.plugins.length; i++) {
				for (int j = 0; j < messages.length; j++) {
					this.send(this.plugins[i].onMessage(messages[j]));
				}
			}
		}
	}
}