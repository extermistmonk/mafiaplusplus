package com.sc2mafia.mafiaplusplus.event;

import java.util.EventObject;

import com.sc2mafia.mafiaplusplus.Player;

public class SystemMessageEvent extends EventObject {

    private static final long serialVersionUID = -4971767742918062990L;
    
    Player[] receivers;
    String message;
    
    public SystemMessageEvent(Object source, Player[] receivers, String message) {
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
