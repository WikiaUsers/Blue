package com.blue.classes;

import com.blue.interfaces.BlueMessageInterface;

class BlueMessage implements BlueMessageInterface {
    private String message;
    private String user;

    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }

    public String getUser() { return this.user; }
    public void setUser(String user) { this.user = user; }
}
