package com.blue.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for commands sent to the bot
 * Every plugin command must extend this class in a manner similar to:
 * Command c = new Command(config) {
 *     @Override
 *     protected void execute() {
 *         // Here add all things the command should do
 *     }
 * };
 */
public abstract class Command {
	public static final String SCOPE_ALL  = "all",
	                           SCOPE_MAIN = "main",
	                           SCOPE_PM   = "pm";
	
	private final String prefix, permission;
	private final Pattern pattern;
	private final boolean main, pm;
	@SuppressWarnings("unused")
	private final Plugin plugin;
	
	/**
	 * Class constructor
	 * @param  config  configuration of the command
	 */
	public Command(final JSONObject config, final Plugin plugin) throws JSONException {
		this.plugin = plugin;
		this.pattern = Pattern.compile(config.getString("pattern"));
		this.prefix = config.getString("prefix");
		this.permission = config.getString("permission");
		String scope = config.getString("scope");
		boolean all = scope.equals(SCOPE_ALL);
		main = all || scope.equals(SCOPE_MAIN);
		pm   = all || scope.equals(SCOPE_PM);
	}
	
	/**
	 * Try matching the message with command configuration
	 * If successful, will jump to main command body
	 * @param  message  message that was sent
	 * @param  user     user that sent the message
	 * @param  room     room in which the message was sent
	 */
	public void executeMatch(final String message, final User user, final Room room) {
		if(
			user.is(permission) &&
			(
				(room.isPrivate() && pm) ||
				(!room.isPrivate() && main)
			) &&
			message.startsWith(prefix)
		) {
			Matcher m = pattern.matcher(message.substring(prefix.length()));
			if(m.find()) execute();
		}
	}
	
	/**
	 * Main method body
	 * This method should be extended by plugins
	 */
	protected abstract void execute();
	
}