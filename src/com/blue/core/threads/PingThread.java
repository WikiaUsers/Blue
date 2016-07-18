package com.blue.core.threads;

import com.blue.core.Room;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Used to ping the chat server so room's session doesn't expire
 */
public class PingThread extends Thread {
	
	private Room room;
	private boolean dead;
	
	/**
	 * Class constructor
	 * @param  room  reference to the room object
	 */
	public PingThread(final Room room) {
		super();
		this.room = room;
	}
	
	/**
	 * Constantly pings the room
	 * @see {@code Room#ping()}
	 */
	public void run() {
		super.run();
		while(!dead) {
			try { room.ping(); }
			catch(UnirestException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * Kills the thread
	 */
	public void kill() { dead = true; }
	
}
