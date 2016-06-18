package com.blue.bot.classes;

import com.blue.bot.interfaces.BlueConnectorInfoInterface;

class BlueConnectorInfo implements BlueConnectorInfoInterface {
    private String channel;
    private String username;

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getRoom() { return this.room; }
    public void setRoom(String room) { this.room = room; }
}
