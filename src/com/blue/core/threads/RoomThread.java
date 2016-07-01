package com.blue.core.threads;

import com.blue.core.Room;

/**
 * Runs room updates constantly
 */
public class RoomThread extends Thread {
	
	Room room;
	
	/**
	 * Class constructor
	 * @param  room  reference to the room to update
	 */
	public RoomThread(Room room) {
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
		while(true) room.update();
	}
	
}
