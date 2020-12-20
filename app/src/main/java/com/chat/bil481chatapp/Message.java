package com.chat.bil481chatapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_SENDER = "sender";
    public static final String USER_RECEIVER = "recipient";
    public static final String MESSAGE = "message";

    public String getUserSender() {
        return getString(USER_SENDER);
    }

    public void setUserSender(String userId) {
        put(USER_SENDER, userId);
    }

    public String getUserReceiver() {
        return getString(USER_RECEIVER);
    }

    public void setUserReceiver(String receiver) {
        put(USER_RECEIVER, receiver);
    }

    public String getMessage() {
        return getString(MESSAGE);
    }

    public void setMessage(String body) {
        put(MESSAGE, body);
    }
}