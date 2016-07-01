package com.blue.core;

/**
 * Interface for all the plugins
 * It defines basic hooks plugins can use
 * @see {@code Plugin}
 */
public interface PluginInterface {
	
	/**
	 * When a user sends a message
	 * @param  room     room in which the message was sent
	 * @param  user     sender
	 * @param  message  message that was sent
	 */
	public void onMessage(Room room, User user, String message);
	
	/**
	 * When bot finishes initializing the room
	 * @param  room  room that was initialized
	 */
	public void onInitial(Room room);
	
	/**
	 * When a user joins
	 * @param  room  room in which the user joined
	 * @param  user  user that joined
	 */
	public void onJoin(Room room, User user);
	
	/**
	 * When a user parts (exits)
	 * @param  room  room from which user exited
	 * @param  user  user that exited
	 */
	public void onPart(Room room, User user);
	
	/**
	 * When log off signal is received
	 * @param  room  room in which the signal was received
	 * @param  user  user that has sent the signal
	 */
	public void onLogout(Room room, User user);
	
	/**
	 * When a user gets kicked out
	 * @param  room      room from which the user was kicked
	 * @param  user      user that was kicked
	 * @param  executor  moderator that kicked the user
	 */
	public void onKick(Room room, User user, User executor);
	
	/**
	 * When a user gets banned
	 * @param  room      room from which the user was banned
	 * @param  user      user that was banned
	 * @param  executor  moderator that banned the user
	 * @param  reason    ban reason
	 * @param  length    ban length
	 */
	public void onBan(Room room, User user, User executor, String reason, String length);
	
	/**
	 * When the bot exits the room
	 * @param  room  room from which the bot exited
	 */
	public void onRoomExit(Room room);
	
	/**
	 * When user status gets updated
	 * @param  room  room in which the status updated
	 * @param  user  user that updated the status
	 */
	public void onUpdateUser(Room room, User user);
	
}
