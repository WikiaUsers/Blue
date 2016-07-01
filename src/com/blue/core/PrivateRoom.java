package com.blue.core;

import java.util.Map;

/**
 * Private room class
 */
public class PrivateRoom extends Room {
	
	/**
	 * Class constructor
	 * @param  id         iD of the room
	 * @param  mid        index of the room in the {@code RoomManager.rooms} array
	 * @param  subdomain  subdomain of the wiki the private room is running on
	 * @param  mainRoom   main room of the wiki
	 * @param  users      users in the room
	 */
	public PrivateRoom(int id, int mid, String subdomain, MainRoom mainRoom, Map<String, User> users) {
		super(id, mid, subdomain, mainRoom.getUser());
		headers = mainRoom.getHeaders();
		options = mainRoom.getOptions();
		this.users = users;
		setup();
	}
	
}