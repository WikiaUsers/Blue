package com.blue.tests;

import com.blue.core.*;

public class TestConnector extends Connector {
	private String[] messages = { "Hello", "How are you?" };
	
	public void send(String message) {
		System.out.println(message);
	}
	
	public String[] getMessages() {
		if (this.messages.length > 0) {
			return this.messages;
		}
		
		String[] blank = new String[0];
		return blank;
	}
	
	public boolean hasMessages() {
		if (this.messages.length > 0) {
			return true;
		}
		
		return false;
	}
}