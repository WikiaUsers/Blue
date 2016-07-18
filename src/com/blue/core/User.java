package com.blue.core;

public class User {
	public static final String  ALL     = "all",
	                            MOD 	= "mod",
	                            ADMIN 	= "admin",
	                            STAFF 	= "staff",
	                            DEV 	= "dev";
	
	public final String name;
	private final boolean mod, admin, staff, dev;
	private boolean ignored;
	
	/**
	 * Class constructor
	 * @param  username  name of the user
	 * @param  mod       if the user is a chat moderator
	 * @param  admin     if the user is an admin
	 * @param  staff     if the user is a Staff member
	 */
	public User(final String username, final boolean mod, final boolean admin, final boolean staff) {
		this.name = username;
		this.mod = mod;
		this.admin = admin;
		this.staff = staff;
		this.dev = username.equals("Lil' Miss Raricow") // Initial developer
		        || username.equals("Lil' Miss Rarity")  // If initial developer ever decides to come back to the old username
		        || username.equals("KockaAdmiralac");   // :^)
		this.ignored = false; // TODO: Read the ignore data
	}
	
	public boolean is(String right) {
		// TODO: Currently, mods cannot be ignored?
		switch(right) {
			case ALL:   return !ignored;
			case MOD: 	return mod || admin || staff || dev;
			case ADMIN:	return        admin || staff || dev;
			case STAFF:	return                 staff || dev;
			case DEV: 	return                          dev;
			default: 	return false;
		}
	}
	
}
