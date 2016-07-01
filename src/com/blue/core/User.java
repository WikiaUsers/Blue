package com.blue.core;

public class User {
	public static final String  MOD 	= "mod",
	                            ADMIN 	= "admin",
	                            STAFF 	= "staff",
	                            DEV 	= "dev";
	
	public String name;
	private boolean mod, admin, staff, dev;
	
	public User(String username, boolean mod, boolean admin, boolean staff) {
		this.name = username;
		this.mod = mod;
		this.admin = admin;
		this.staff = staff;
		this.dev = username.equals("Lil' Miss Raricow");
	}
	
	public boolean is(String right) {
		switch(right) {
			case MOD: 	return mod || admin || staff || dev;
			case ADMIN:	return        admin || staff || dev;
			case STAFF:	return                 staff || dev;
			case DEV: 	return                          dev;
			default: 	return false;
		}
	}
	
}
