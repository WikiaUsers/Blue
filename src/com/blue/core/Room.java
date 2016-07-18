package com.blue.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.blue.core.threads.*;

/**
 * Handles requests to the chat server.
 * Isn't instatiated directly, but through the inherited
 * classes, {@code MainRoom} and {@code PrivateRoom}
 */
public abstract class Room {
	
	/**
	 * ===============================================================================
	 * VARIABLES
	 * ===============================================================================
	 */
	
	// TODO: I dunno how to use Javadoc to document variables lmao
	private final String SESSION_RESET = "Session ID unknown";
	
	protected int id;
	protected String subdomain;
	protected String baseURI;
	protected Account user;
	protected Map<String, String> headers;
	protected Map<String, Object> options;
	protected Map<String, User> users;
	private boolean isPrivate;
	private Plugin[] plugins;
	private RoomThread mainThread;
	private PingThread pingThread;
	private List<MessageThread> threads;
	private boolean initialized;
	private boolean dead;
	
	/**
	 * ===============================================================================
	 * SETUP
	 * ===============================================================================
	 */
	
	/**
	 * Class constructor
	 * @param id         the iD of the room
	 * @param mid        the index in the RoomManager's array of the room
	 * @param subdomain  the subdomain room is running on
	 * @param account    the account room is using
	 */
	public Room(int id, String subdomain, String account) {
		this.id = id;
		this.isPrivate = !(id == -1);
		this.subdomain = subdomain;
		this.user = AccountManager.getAccount(account);
		this.users = new HashMap<String, User>();
		this.plugins = PluginManager.getPluginsBySubdomain(subdomain);
		this.threads = new ArrayList<MessageThread>();
	}
	
	/**
	 * Setups the room. This method is overriden by
	 * {@code MainRoom} and {@code PrivateRoom}
	 */
	protected void setup() {
		options.put("roomId", Integer.toString(id));
		try {
			HttpResponse<String> response = get();
			options.put("sid", new JSONObject(response.getBody().substring(5)).getString("sid"));
			List<String> setCookie = response.getHeaders().get("Set-Cookie");
			String cookies = "";
			for(String cookie : setCookie) cookies += cookie + ", ";
			headers.put("Cookie", cookies.substring(0, cookies.length() - 2));
			mainThread = new RoomThread(this);
			mainThread.run();
		}
		catch(UnirestException e) { e.printStackTrace(); }
		catch(JSONException e) { e.printStackTrace(); }
	}
	
	/**
	 * Sends a GET request to the chat server
	 * @return                   the response of the query
	 * @throws UnirestException  if connection didn't succeed
	 */
	private HttpResponse<String> get() throws UnirestException {
		System.out.println(options);
		System.out.println(headers);
		return Unirest.get(baseURI)
			.headers(headers)
			.queryString(options)
			.queryString("t", Util.getCacheBuster())
			.asString();
	}
	
	/**
	 * Updates the room. This method is usually being called
	 * from a {@code RoomThread}
	 */
	public void update() {
		try {
			HttpResponse<String> response = get();
			String body = response.getBody();
			int index = body.indexOf((char)65533);
			if(body.toCharArray()[0] != (char)0 && index == -1 && body.indexOf(SESSION_RESET) > -1) {
				// TODO: Handle a reset of the session
				// Probably close the thread and delete the room from the room array
				// Unless if the room was the main room, in that case, restart it
				System.out.println("Session reset");
				kill();
				return;
			}
			String message = body.substring(index + 1);
			if(message.length() > 2 && message.substring(0, 2).equals("42")) {
				threads.add(new MessageThread(this, message.substring(2), threads.size()));
				threads.get(threads.size() - 1).run();
			}
				
		}
		catch(UnirestException e) { e.printStackTrace(); }
	}
	
	/**
	 * ===============================================================================
	 * EVENT HANDLING
	 * ===============================================================================
	 */
	
	/**
	 * When packet is received
	 * @param  message  received message in JSON format
	 */
	public void onPacket(String message) {
		if(dead) return;
		try {
			System.out.println(message);
			JSONObject msg = new JSONArray(message).getJSONObject(1);
			String event = msg.getString("event");
			JSONObject data = new JSONObject(msg.getString("data"));
			JSONObject attrs = data.getJSONObject("attrs");
			// TODO: Ewwwwwwwwwwwww
			switch(event) {
				case "chat:add":
					if(data.get("id") != JSONObject.NULL) onMessage(
						attrs.getString("text"),
						users.get(attrs.getString("name"))
					);
					break;
				case "initial":
					onInitial(data);
					break;
				case "join":
					onJoin(new User(
						attrs.getString("name"),
						attrs.getBoolean("isModerator"),
						attrs.getBoolean("canPromoteModerator"),
						attrs.getBoolean("isStaff")
					));
					break;
				case "part":
					onPart(users.get(attrs.getString("name")));
					break;
				case "logout":
					onLogout(users.get(attrs.getString("name")));
					break;
				case "kick":
					onKick(
						users.get(attrs.getString("kickedUserName")),
						users.get(attrs.getString("moderatorName"))
					);
					break;
				case "ban":
					onBan(
						users.get(attrs.getString("kickedUserName")),
						users.get(attrs.getString("moderatorName")),
						attrs.getString("time"),
						attrs.getString("reason")
					);
					break;
//				case "updateUser":
//					onUpdateUser();
//					break;
				case "openPrivateRoom":
					Map<String, User> roomUsers = new HashMap<String, User>();
					JSONArray userArray = attrs.getJSONArray("users");
					for(int i = 0; i < userArray.length(); ++i) {
						String user = userArray.getString(i);
						roomUsers.put(user, users.get(user));
					}
					onOpenPrivateRoom(
						attrs.getInt("roomId"),
						users
					);
					break;
			}
		}
		catch(JSONException e) { e.printStackTrace(); }
	}
	
	/**
	 * When message is received
	 * @param  message  received message
	 * @param  sender   user that sent the message
	 */
	private void onMessage(String message, User sender) {
		// for(Plugin p : plugins) p.onMessage(this, sender, message);
	}
	
	/**
	 * When initial chatroom data about users and
	 * scrollback is received
	 * @param  data  initial data
	 */
	private void onInitial(JSONObject data) {
		System.out.println("Initial");
		try {
			JSONArray usersArray = data.getJSONObject("collections").getJSONObject("users").getJSONArray("models");
			for(int i = 0; i < usersArray.length(); ++i) {
				JSONObject attrs = usersArray.getJSONObject(i).getJSONObject("attrs");
				String username = attrs.getString("name");
				users.put(username, new User(
					username,
					attrs.getBoolean("isModerator"),
					attrs.getBoolean("canPromoteModerator"),
					attrs.getBoolean("isStaff")
				));
			}
			initialized = true;
			pingThread = new PingThread(this);
			pingThread.run();
			// for(Plugin p : plugins) p.onInitial(this);
		}
		catch(JSONException e) { e.printStackTrace(); }
	}
	
	/**
	 * When a user joins the chat
	 * @param  user  user that joined
	 */
	private void onJoin(User user) {
		if(user.name.equals(this.user.name) && !initialized) {
			try { postCommand(new JSONObject(), "initquery"); }
			catch(UnirestException e) { e.printStackTrace(); }
			catch(JSONException e) { e.printStackTrace(); }
		}
		users.put(user.name, user);
		// for(Plugin p : plugins) p.onJoin(this, user);
	}
	
	/**
	 * When a user parts from (exits) the chat
	 * @param  user  user that exited
	 */
	private void onPart(User user) {
		users.remove(user);
		// for(Plugin p : plugins) p.onPart(this, user);
	}
	
	/**
	 * When a log off signal is received
	 * @param  user  user that has sent the log off signal
	 */
	private void onLogout(User user) {
		users.remove(user);
		// for(Plugin p : plugins) p.onLogout(this, user);
	}
	
	/**
	 * When a user gets kicked out
	 * @param  user      user that got kicked out
	 * @param  executor  moderator that kicked the user
	 */
	private void onKick(User user, User executor) {
		// for(Plugin p : plugins) p.onKick(this, user, executor);
	}
	
	/**
	 * When a user gets banned
	 * @param  user      user that was banned
	 * @param  executor  moderator that banned the user
	 * @param  reason    ban reason
	 * @param  length    ban length
	 */
	private void onBan(User user, User executor, String length, String reason) {
		// for(Plugin p : plugins) p.onBan(this, user, executor, reason, length);
	}
	
//	private void onUpdateUser() {
//		
//	}
	
	/**
	 * When a private room is opened
	 * @param  roomId  iD of the private room
	 * @param  users   users in the room
	 */
	private void onOpenPrivateRoom(int roomId, Map<String, User> users) {
		RoomManager.addPrivateRoom(roomId, subdomain, this, users);
	}
	
	/**
	 * ===============================================================================
	 * POSTING
	 * ===============================================================================
	 */
	
	/**
	 * Sends a POST request to the chat server with specified string as body
	 * @param   body              the body for the POST request
	 * @throws  UnirestException  if the request was unsuccessful
	 */
	private void post(JSONObject body) throws UnirestException, JSONException {
		System.out.println(options);
		System.out.println(headers);
		Unirest.post(baseURI)
			.headers(headers)
			.queryString(options)
			.queryString("t", Util.getCacheBuster())
			.body(body == null ? "2" : "42" +
				new JSONArray()
					.put("message")
					.put(new JSONObject()
						.put("id", JSONObject.NULL)
						.put("attrs", body)
						.toString()
					)
			)
			.asString();
	}
	
	/**
	 * Sends a POST request to the chat server with JSON as body
	 * @param   body              object to send as a body
	 * @throws  UnirestException  if the request was unsuccessful
	 * @throws  JSONException     if JSON wasn't able to be handled correctly
	 */
	private void post(JSONObject body, String msgType) throws UnirestException, JSONException { post(body.put("msgType", msgType)); }
	
	/**
	 * Sends a ping to the chat server.
	 * If a ping wasn't sent to the chat server, at some point client will
	 * receive a {@code SESSION_RESET} in the body and will have to
	 * reconnect to the chat server
	 * @throws  UnirestException    if the request was unsuccessful
	 * @see     {@code PingThread}
	 */
	public void ping() throws UnirestException {
		try { post(null); }
		catch(JSONException e) { /* This will literally never happen lol */ }
	}
	
	/**
	 * POSTs a command to the chat server
	 * @param   obj               request body
	 * @param   command           command's type
	 * @throws  JSONException     if JSON wasn't able to be handled correctly
	 * @throws  UnirestException  if the request was unsuccessful
	 */
	public void postCommand(JSONObject obj, String command) throws JSONException, UnirestException {
		obj.put("command", command);
		post(obj, "command");
	}
	
	/**
	 * Sends a message to the room
	 * @param  message  message to send
	 */
	public void sendMessage(String message) {
		try {
			post(new JSONObject()
				.put("text", message)
				.put("name", user.name),
			"chat");
		}
		catch(JSONException e) { e.printStackTrace(); }
		catch(UnirestException e) { e.printStackTrace(); }
	}
	
	/**
	 * Kicks a user from the room
	 * @param  user  user to kick
	 */
	public void kick(String user) {
		try { postCommand(new JSONObject().put("userToKick", user), "kick"); }
		catch(JSONException e) { e.printStackTrace(); }
		catch(UnirestException e) { e.printStackTrace(); }
	}
	
	/**
	 * Kicks a user from the room
	 * @param  user  user to kick
	 */
	public void kick(User user) { kick(user.name); }
	
	/**
	 * Bans a user from the room
	 * @param  user    user to ban
	 * @param  reason  ban reason
	 * @param  length  ban length
	 */
	public void ban(String user, String reason, String length) {
		try {
			postCommand(new JSONObject()
				.put("userToBan", user)
				.put("time", length)
				.put("reason", reason)
			, "ban");
		}
		catch(JSONException e) { e.printStackTrace(); }
		catch(UnirestException e) { e.printStackTrace(); }
	}
	
	/**
	 * Bans a user from the room
	 * @param  user    user to ban
	 * @param  reason  ban reason
	 * @param  length  ban length
	 */
	public void ban(User user, String reason, String length) { ban(user.name, reason, length); }
	
	/**
	 * Bans a user from the room
	 * @param  user    user to ban
	 * @param  reason  ban reason
	 * @param  length  ban length
	 */
	public void ban(String user, String reason) { ban(user, reason, "31536000000"); }
	
	/**
	 * Bans a user from the room
	 * @param  user    user to ban
	 * @param  reason  ban reason
	 * @param  length  ban length
	 */
	public void ban(User user, String reason) { ban(user.name, reason, "31536000000"); }
	
	/**
	 * Opens a private room
	 * @param  user  user to open the private room with
	 */
	public void openPrivateRoom(String user) { /* TODO: Implement */ }
	
	/**
	 * Opens a private room
	 * @param  user  user to open the private room with
	 */
	public void openPrivateRoom(User user) { openPrivateRoom(user.name); }
	
	/**
	 * Closes/restarts the room
	 */
	private void kill() {
		try {
			dead = true;
			if(pingThread != null) pingThread.kill();
			mainThread.kill();
			for(MessageThread t : threads) t.join();
			// for(Plugin p : plugins) p.onRoomExit(this);
			System.out.println("Ded");
		}
		catch(InterruptedException e) { e.printStackTrace(); }
	}
	
	/**
	 * ===============================================================================
	 * GETTERS AND SETTERS
	 * ===============================================================================
	 */
	
	/**
	 * Getter for {@code id}
	 * @return  the room's iD
	 */
	public int getId() { return this.id; }
	
	/**
	 * Getter for {@code isPrivate}
	 * @return  if the room is private
	 */
	public boolean isPrivate() { return this.isPrivate; }
	
	/**
	 * Removes the thread from the {@code threads} list
	 * @param index
	 */
	public void removeThread(int index) {
		try { threads.get(index).join(); }
		catch(InterruptedException e) { e.printStackTrace(); }
		threads.remove(index);
	}
	
}