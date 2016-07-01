package com.blue.core.threads;

import com.blue.core.Room;

/**
 * Used to run actions on a received message asynchronously
 */
public class MessageThread extends Thread {
	
	private Room room;
	private String message;
	private int index;
	
	/**
	 * Class constructor
	 * TODO: Use mid?
	 * @param  room     reference to the room
	 * @param  message  message to process
	 * @param  index    index of the thread in threads array
	 */
	public MessageThread(Room room, String message, int index) {
		super();
		this.room = room;
		this.message = message;
		this.index = index;
	}
	
	/**
	 * Sends the message to the room to process and then closes the thread
	 * @see {@code Room#onPacket(String)}
	 * @see {@code Room#removeThread(int)}
	 */
	public void run() {
		super.run();
		room.onPacket(message);
		room.removeThread(index);
	}
	
}