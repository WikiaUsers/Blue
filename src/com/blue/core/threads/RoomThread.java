package com.blue.core.threads;

import com.blue.core.Room;

/**
 * Runs room updates constantly
 */
public class RoomThread extends Thread {
	
	private final Room room;
	private boolean ded;
	
	/**
	 * Class constructor
	 * @param  room  reference to the room to update
	 */
	public RoomThread(final Room room) {
		super();
		this.room = room;
	}
	
	/**
	 * Runs room updates
	 * @see {@code Room#update()}
	 */
	public void run() {
		super.run();
		// TODO: Add a return value to the update method
		// so the loop isn't infinite
		while(!ded) room.update();
	}
	
	/**
	 * Kills the thread
	 */
	public void kill() { ded = true; }
	
}
