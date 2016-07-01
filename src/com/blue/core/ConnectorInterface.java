package com.blue.core;

interface ConnectorInterface {
	void send(String message);
	boolean hasMessages();
	String[] getMessages();
}
