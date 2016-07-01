package com.blue.core;

import java.nio.charset.StandardCharsets;

/**
 * Utility class for many minor but important functionalities
 */
public class Util {
	
	public static final String API_PATH = "api.php";
	public static final String WIKIA_PATH = "wikia.php";
	private static int cachebuster = Integer.MIN_VALUE;
	
	/**
	 * Returns a full URL of a wiki, used in some requests
	 * and probably will be used in future by plugins
	 * @param  subdomain  wiki's subdomain
	 * @return            full URL of the wiki
	 */
	public static String fullURL(String subdomain) { return "http://" + subdomain + ".wikia.com"; }
	
	/**
	 * Returns a full URL of a wiki, used in some requests
	 * and probably will be used in future by plugins
	 * @param  subdomain  wiki's subdomain
	 * @param  append     string to append at the end of URL
	 * @return            full URL of the wiki
	 */
	public static String fullURL(String subdomain, String append) { return fullURL(subdomain) + "/" + append; }
	
	/**
	 * Returns an integer used to bust cache on HTTP requests
	 * @return  the cachebuster
	 */
	public static String getCacheBuster() { return Integer.toString(cachebuster ++); }
	
	/**
	 * Formats a message so it can be sent through socket.io
	 * WARNING: Stupid formats ahead
	 * @param  message  message to format
	 * @return          formatted message
	 */
	public static String formatMessage(String message) {
		byte[] bytes = message.getBytes(StandardCharsets.US_ASCII);
		String end = "",
			   size = Integer.toString(message.length());
		end += (char)0;
		for(int c : size.chars().toArray()) end += (char)c;
		end += (char)255;
		for(byte b : bytes) end += (char)b;
		return end;
	}
	
}
