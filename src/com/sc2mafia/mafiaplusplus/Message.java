package com.sc2mafia.mafiaplusplus;

import java.util.ArrayList;

public class Message {

    private String message;
    private Player sender;
    private ArrayList<Player> receiverList;
    private ArrayList<Player> anonymousReceiverList;
    private ArrayList<Player> overrideList;
    private boolean anonymous = false;
    private boolean killed = false;
    
    public Message(String message, Player sender, boolean anonymous) {
	this.message = message;
	this.sender = sender;
	this.anonymous = anonymous;
	receiverList = new ArrayList<Player>();
	anonymousReceiverList = new ArrayList<Player>();
	overrideList = new ArrayList<Player>();
    }
    
    public void addReceiver(Player player) {
	receiverList.add(player);
    }
    public void addAnonymousReceiver(Player player) {
	anonymousReceiverList.add(player);
    }
    
    public void addOverrideReceiver(Player player) {
	overrideList.add(player);
    }

    public String getMessage() {
        return message;
    }

    public Player getSender() {
        return sender;
    }

    public Player[] getReceiverList() {
        return receiverList.toArray(new Player[receiverList.size()]).clone();
    }

    public Player[] getAnonymousReceiverList() {
        return anonymousReceiverList.toArray(new Player[anonymousReceiverList.size()]).clone();
    }

    public Player[] getOverrideList() {
        return overrideList.toArray(new Player[overrideList.size()]).clone();
    }

    public boolean isAnonymous() {
        return anonymous;
    }
    
    public boolean isKilled() {
	return killed;
    }
    
    public void kill() {
	killed = true;
    }
    
}
