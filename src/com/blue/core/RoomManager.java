package com.blue.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for managing rooms
 * Here are all rooms initialized and added to the {@code rooms} array
 */
public class RoomManager {
	
	public static List<Room> rooms;
	
	/**
	 * Initializer
	 */
	public static void init() {
		rooms = new ArrayList<Room>();
	}
	
	/**
	 * Initializes and adds a main room to the {@code rooms} array
	 * @param  subdomain  subdomain of the wiki the room is in
	 * @param  account    account name to use for connecting to the wiki
	 */
	public static void addMainRoom(String subdomain, String account) {
		rooms.add(new MainRoom(rooms.size(), subdomain, account));
	}
	
	/**
	 * Initializes and adds a private room to the {@code rooms} array
	 * @param  id         iD of the room
	 * @param  subdomain  subdomain of the wiki the room is in
	 * @param  mainRoom   reference to the main room, not saved in the actual room
	 * @param  users      list of users in the room
	 */
	public static void addPrivateRoom(int id, String subdomain, Room mainRoom, Map<String, User> users) {
		rooms.add(new PrivateRoom(id, rooms.size(), subdomain, (MainRoom)mainRoom, users));
	}
	
}
