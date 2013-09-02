package com.sc2mafia.mafiaplusplus.core;

import java.util.EventObject;

public class SystemMessage extends EventObject {

    private static final long serialVersionUID = -7234287211786587126L;
    
    Player[] receivers;
    String message;
    
    public SystemMessage(Object source, Player[] receivers, String message) {
	super(source);
	this.receivers = receivers;
	this.message = message;
    }

    public Player[] getReceiver() {
        return receivers;
    }

    public String getMessage() {
        return message;
    }

}
