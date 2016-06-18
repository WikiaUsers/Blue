package com.blue.bot.interfaces;

import com.blue.bot.classes.BlueMessage;
import com.blue.bot.classes.BlueConnectorInfo;

/**
 * To do:
 *     - Create a class that holds configuration options such as rooms/channels
 *     - Create a message class that holds information about a message such as time, user, etc
 */

interface BlueConnectorInterface {
    void initialize(BlueConnectorInfo info);                                    // Initialize connections, joins, etc
    boolean hasMessages();                                                      // Should return whether or not there are messages in the queued
    boolean hasModerator();                                                     // Should return whether or not the current user is a moderator
    Array<BlueMessage> getMessages();                                           // Messages should be queued so that we can retrieve them periodically
    void sendMessage(String message);                                           // Should process sending a message
    void sendKick(String user, String reason);                                  // Should process removing a user from the room
    void sendBan(String user, String reason, double length);                    // Should process banning a user from the room
}
