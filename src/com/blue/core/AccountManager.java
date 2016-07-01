package com.blue.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Class for managing accounts bots can use
 * Made so one account can be easily used for multiple wikis
 */
public class AccountManager {
	
	private static Map<String, Account> accounts;
	
	/**
	 * Initializer
	 */
	public static void init() {
		accounts = new HashMap<String, Account>();
	}
	
	/**
	 * Registers a new account to be used for further purposes
	 * @param  username  account username
	 * @param  password  account password
	 */
	public static void registerNewAccount(String username, String password) {
		try {
			HttpResponse<JsonNode> response = Unirest.post("http://community.wikia.com/api.php")
				.queryString("action", "login")
				.queryString("format", "json")
				.queryString("lgname", username)
				.queryString("lgpassword", password)
				.asJson();
			HttpResponse<JsonNode> response2 = Unirest.post("http://community.wikia.com/api.php")
				.queryString("action", "login")
				.queryString("format", "json")
				.queryString("lgname", username)
				.queryString("lgpassword", password)
				.queryString("lgtoken", response.getBody().getObject().getJSONObject("login").getString("token"))
				.asJson();
			accounts.put(username, new Account(username, response2.getHeaders().get("Set-Cookie")));
		}
		catch(UnirestException e) { e.printStackTrace(); }
		catch(JSONException e) { e.printStackTrace(); }
	}
	
	/**
	 * Getter for an account from {@code accounts}
	 * @param  username  username of the account to get
	 * @return           the requested account
	 */
	public static Account getAccount(String username) { return accounts.get(username); }
	
}