package com.blue.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * A wiki's main room.
 * Here is initialized everything wiki-widely important
 */
public class MainRoom extends Room {
	
	/**
	 * Class constructor
	 * @param  mid        index of the room in the {@code RoomManager.rooms} array
	 * @param  subdomain  wiki's subdomain
	 * @param  account    account to use to connect
	 */
	public MainRoom(String subdomain, String account) {
		super(-1, subdomain, account);
		setup();
	}
	
	/**
	 * Sets up the important room data
	 */
	protected void setup() {
		setupHeaders();
		setupOptions();
		getWikiId();
		fetchChatInfo();
		super.setup();
	}
	
	/**
	 * Sets up the headers to be sent in GET and POST requests
	 */
	private void setupHeaders() {
		headers = new HashMap<String, String>();
		headers.put("User-Agent", "Blue-bot v0.1 [https://github.com/Blue-bot/Blue]");
		headers.put("Cookie", user.getFormattedCookies());
		headers.put("Content-Type", "application/octet-stream");
		headers.put("Accept", "*/*");
		headers.put("Pragma", "no-cache");
		headers.put("Cache-Control", "no-cache");
	}
	
	/**
	 * Sets up the request options to use in GET and POST requests
	 */
	private void setupOptions() {
		options = new HashMap<String, Object>();
		options.put("name", user.name);
		options.put("EIO", "3");
		options.put("transport", "polling");
	}
	
	/**
	 * Gets the iD of the wiki
	 */
	private void getWikiId() {
		try {
			HttpResponse<JsonNode> response = Unirest.get(Util.fullURL(subdomain, Util.API_PATH))
				.queryString("action", "query")
				.queryString("meta", "siteinfo")
				.queryString("siprop", "wikidesc")
				.queryString("format", "json")
				.asJson();
			String wikiId = response.getBody().getObject().getJSONObject("query").getJSONObject("wikidesc").getString("id");
			options.put("serverId", wikiId);
			options.put("wikiId", wikiId);
		}
		catch(UnirestException e) { e.printStackTrace(); }
		catch(JSONException e) { e.printStackTrace(); }
	}
	
	/**
	 * Fetches information about the chat, such as main room iD,
	 * base URI to request chat info from, chat key etc.
	 */
	private void fetchChatInfo() {
		try {
			HttpResponse<JsonNode> response = Unirest.get(Util.fullURL(subdomain, Util.WIKIA_PATH))
				.queryString("controller", "Chat")
				.queryString("format", "json")
				.headers(headers)
				.asJson();
			JSONObject data = response.getBody().getObject();
			id = data.getInt("roomId");
			options.put("key", data.getString("chatkey"));
			baseURI = "http://" + data.getString("chatServerHost") + "/socket.io/";
		}
		catch(UnirestException e) { e.printStackTrace(); }
		catch(JSONException e) { e.printStackTrace(); }
	}
	
	/**
	 * Getter for the username of the bot account
	 * This method is used for sharing data with private rooms.
	 * @return  the username of the bot account
	 * @see     {@code PrivateRoom}
	 */
	public String getUser() { return user.name; }
	
	/**
	 * Getter for headers. Removes room-specific options before returning
	 * This method is used for sharing data with private rooms.
	 * @return  headers
	 * @see     {@code PrivateRoom}
	 */
	public Map<String, String> getHeaders() {
		Map<String, String> headers = this.headers;
		headers.remove("set-cookie");
		return headers;
	}
	
	/**
	 * Getter for request options. Removes room-specific options before returning
	 * @return  request options
	 * @see     {@code PrivateRoom}
	 */
	public Map<String, Object> getOptions() {
		Map<String, Object> options = this.options;
		options.remove("sid");
		return options;
	}
	
}