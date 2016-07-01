package com.blue.tests;

import org.json.JSONObject;

import com.blue.core.Plugin;
import com.blue.core.Room;
import com.blue.core.User;

public class TestPlugin extends Plugin {
	public TestPlugin(JSONObject config) {
		super(config);
		System.out.println("Lol");
	}
	
	public String onMessage(String message) {
		return message;
	}

	@Override
	public void onMessage(Room room, User user, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitial(Room room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoin(Room room, User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPart(Room room, User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogout(Room room, User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKick(Room room, User user, User executor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBan(Room room, User user, User executor, String reason,
			String length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomExit(Room room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateUser(Room room, User user) {
		// TODO Auto-generated method stub
		
	}
}