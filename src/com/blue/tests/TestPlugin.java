package com.blue.tests;

import org.json.JSONObject;

import com.blue.core.Plugin;

public class TestPlugin extends Plugin {
	public TestPlugin(JSONObject config) {
		super(config);
		System.out.println("Lol");
	}
}