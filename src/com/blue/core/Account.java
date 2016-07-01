package com.blue.core;

import java.util.List;

/**
 * Model for accounts bots can log in to chats with
 */
public class Account {
	
	public final String name;
	private final List<String> cookieJar;
	
	/**
	 * Class constructor
	 * @param name       username of the account
	 * @param cookieJar  cookies the account can use to log in to the chat server
	 */
	public Account(String name, List<String> cookieJar) {
		this.name = name;
		this.cookieJar = cookieJar;
	}
	
	/**
	 * Formats the cookies in the way they can be used in requests to the chat server
	 * @return  formatted cookies
	 */
	public String getFormattedCookies() {
		String result = "";
		for(String cookie : cookieJar) result += cookie.split(";")[0] + "; ";
		return result;
	}
	
}